package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.ChartResponse;
import com.kirchnersolutions.PiCenter.servers.beans.Interval;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.kirchnersolutions.utilities.CalenderConverter.getDaysBetween;
import static com.kirchnersolutions.utilities.CalenderConverter.getDaysInMonth;

@Service
public class ChartService {

    private StatService statService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public ChartService(StatService statService, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.statService = statService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public ChartResponse getChartData(ChartRequest chartRequest) throws Exception{
        return new ChartResponse(generateChartData(chartRequest));
    }

    /**
     *
     * @param chartRequest
     * @return
     */
    private Interval[] generateChartData(ChartRequest chartRequest) throws Exception{
        String start = chartRequest.getFromDate();
        String end = chartRequest.getToDate();
        int interval = getInterval(start, end);
        List<Long> intervals = getTimeIntervals(start, end, interval);
        Future<List<double[]>>[] futures = new Future[5];
        futures[0] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "bedroom"));
        futures[1] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "livingroom"));
        futures[2] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "serverroom"));
        futures[3] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "office"));
        futures[4] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "outside"));
        List<double[]> bedroomValues = futures[0].get();
        List<double[]> livingroomValues = futures[1].get();
        List<double[]> serverroomValues = futures[2].get();
        List<double[]> officeValues = futures[3].get();
        List<double[]> outsideValues = futures[4].get();
        Interval[] intervalList = new Interval[intervals.size()];
        int type = 0;
        if(chartRequest.getType().contains("hum")){
            type = 1;
        }
        int count = 0;
        for(Long time : intervals){
            double br = 0, lr = 0, sr = 0, of = 0, ou = 0;
            //Check for nulls
            if(bedroomValues.get(count) != null){
                br = bedroomValues.get(count)[type];
            }
            if(livingroomValues.get(count) != null){
               lr = livingroomValues.get(count)[type];
            }
            if(serverroomValues.get(count) != null){
                sr = serverroomValues.get(count)[type];
            }
            if(officeValues.get(count) != null){
                of = officeValues.get(count)[type];
            }
            if(outsideValues.get(count) != null){
                ou = outsideValues.get(count)[type];
            }
            intervalList[count] = new Interval(getIntervalString(intervals.get(count), false), br, lr, sr, of, ou, 0);
            count++;
        }
        return intervalList;
    }

    private class ChartValuesThread implements Callable<List<double[]>>{

        private List<Long> intervals;
        private int interval;
        private String room;

        public ChartValuesThread(List<Long> intervals, int interval, String room){
            this.intervals = intervals;
            this.interval = interval;
            this.room = room;
        }

        public List<double[]> call(){
            return getChartValues(generateIntervalWindows(intervals, interval), room);
        }

    }

    /**
     * Returns list of double[] representing the temp([0]) and humidity([1]) at each interval.
     * @param windows
     * @param room
     * @return
     */
    List<double[]> getChartValues(List<long[]> windows, String room){
        List<double[]> values = new ArrayList<>();
        for(long[] window : windows){
            double[] value = statService.getAverage(window, room);
            if(value != null){
                values.add(value);
            }else{
                values.add(new double[]{0, 0});
            }
        }
        return values;
    }

    /**
     * Generates list of periods to average chart points from.
     * @param chartIntervals
     * @param interval
     * @return
     */
    List<long[]> generateIntervalWindows(List<Long> chartIntervals, int interval){
        List<long[]> output = new ArrayList<>();
        long half = interval * CalenderConverter.HOUR;
        half/= 2;
        for(Long period : chartIntervals){
            long[] temp = {period - half, period + half};
            output.add(temp);
        }
        return output;
    }

    /**
     * Generates interval based on time between arguments.
     * @param start
     * @param end
     * @return
     */
    private int getInterval(String start, String end) {
        if (start.equals(end)) {
            return 1;
        } else if (isSameMonth(start, end) && isSameYear(start, end)) {
            int dif = Integer.parseInt(end.split("/")[2]) - Integer.parseInt(start.split("/")[2]);
            if (dif < 6) {
                return 3;
            }
            if (dif < 8) {
                return 6;
            }
            if (dif < 15) {
                return 12;
            }
            return 24;
        } else {
            int dif = getDaysBetween(start, end, "/");
            if (dif < 6) {
                return 3;
            }
            if (dif < 8) {
                return 6;
            }
            if (dif < 15) {
                return 12;
            }
            return 24;
        }
    }

    /**
     * Get list of millis representing each interval between start and end.
     *
     * @param start:    MM/DD/YYYY
     * @param end:      MM/DD/YYYY
     * @param interval: n hours
     * @return
     */
    List<Long> getTimeIntervals(String start, String end, int interval) {
        if (CalenderConverter.getMillisFromDateString(start, "/") > CalenderConverter.getMillisFromDateString(end, "/")) {
            return getTimeIntervals(end, start, interval);
        }
        List<Long> intervals = new ArrayList<>();
        long endMillis = CalenderConverter.getMillisFromDateString(end, "/") + CalenderConverter.DAY;
        long startMillis = CalenderConverter.getMillisFromDateString(start, "/");
        if (start.equals(end)) {
            String[] date = start.split("/");
            if (interval >= 24) {
                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 12, 0, 0));
            } else {
                for (int i = 0; i <= 23; i += interval) {
                    intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
                }
            }
            if(interval > 1){
                date = end.split("/");
                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 23, 0, 0));
            }
            return intervals;
        } else if (isSameMonth(start, end) && isSameYear(start, end)) {
            String[] date = start.split("/");
            for (int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++) {
                if (interval >= 24) {
                    intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), k, Integer.parseInt(date[2]), 12, 0, 0));
                } else {
                    for (int i = 0; i <= 23; i += interval) {
                        intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), k, Integer.parseInt(date[2]), i, 0, 0));
                    }
                }
            }
            if(interval > 1){
                date = end.split("/");
                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 23, 0, 0));
            }
            return intervals;
        } else if (isSameYear(start, end)) {
            int endMonth = Integer.parseInt(end.split("/")[0]);
            int startMonth = Integer.parseInt(start.split("/")[0]);
            String[] date = start.split("/");
            int startDay = Integer.parseInt(date[1]);
            for (int j = startMonth; j <= endMonth; j++) {
                if(j > startMonth){
                    startDay = 1;
                }
                if (j == endMonth) {
                    for (int k = startDay; k <= Integer.parseInt(end.split("/")[1]); k++) {
                        if (interval >= 24) {
                            intervals.add(CalenderConverter.getMillis(j, k, Integer.parseInt(date[2]), 12, 0, 0));
                        } else {
                            for (int i = 0; i <= 23; i += interval) {
                                intervals.add(CalenderConverter.getMillis(j, k, Integer.parseInt(date[2]), i, 0, 0));
                            }
                        }
                    }
                } else {
                    for (int k = startDay; k <= getDaysInMonth(j, Integer.parseInt(date[2])); k++) {
                        if (interval >= 24) {
                            intervals.add(CalenderConverter.getMillis(j, k, Integer.parseInt(date[2]), 12, 0, 0));
                        } else {
                            for (int i = 0; i <= 23; i += interval) {
                                intervals.add(CalenderConverter.getMillis(j, k, Integer.parseInt(date[2]), i, 0, 0));
                            }
                        }
                    }
                }
            }
            if(interval > 1){
                date = end.split("/");
                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 23, 0, 0));
            }
            return intervals;
        } else {
            String[] date = start.split("/");
            int endMonth = Integer.parseInt(end.split("/")[0]);
            int startMonth = Integer.parseInt(start.split("/")[0]);
            int endYear = Integer.parseInt(end.split("/")[2]);
            int startYear = Integer.parseInt(start.split("/")[2]);
            int startDay = Integer.parseInt(date[1]);
            for (int u = startYear; u <= endYear; u++) {
                if(u > startYear){
                    startMonth = 1;
                }
                if(u == endYear){
                    endMonth = Integer.parseInt(end.split("/")[0]);
                }else{
                    endMonth = 12;
                }
                for (int j = startMonth; j <= endMonth; j++) {
                    if (j > startMonth || (u > startYear)) {
                        startDay = 1;
                    }
                    if (j == endMonth && u == endYear) {
                        for (int k = startDay; k <= Integer.parseInt(end.split("/")[1]); k++) {
                            if (interval >= 24) {
                                intervals.add(CalenderConverter.getMillis(j, k, u, 12, 0, 0));
                            } else {
                                for (int i = 0; i <= 23; i += interval) {
                                    intervals.add(CalenderConverter.getMillis(j, k, u, i, 0, 0));
                                }
                            }
                        }
                    } else {
                        for (int k = startDay; k <= getDaysInMonth(j, Integer.parseInt(date[2])); k++) {
                            if (interval >= 24) {
                                intervals.add(CalenderConverter.getMillis(j, k, u, 12, 0, 0));
                            } else {
                                for (int i = 0; i <= 23; i += interval) {
                                    intervals.add(CalenderConverter.getMillis(j, k, u, i, 0, 0));
                                }
                            }
                        }
                    }
                }
            }
            if(interval > 1){
                date = end.split("/");
                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 23, 0, 0));
            }
            return intervals;
        }
    }

    String getIntervalString(long time, boolean withDate){
        String date = CalenderConverter.getMonthDayYearHour(time, "/", ":");
        int hour = Integer.parseInt(date.split(" ")[1].split(":")[0]);
        String hourText = "";
        if(hour > 12){
            hourText = hour - 12 + "PM";
        }else{
            if(hour == 0){
                hourText = date.split(" ")[0] + " 12AM";
            }else if(hour == 12){
                hourText = hour + "PM";
            }else{
                hourText = hour +  "AM";
            }
        }
        if(withDate){
            return date.split(" ")[0] + " " + hourText.trim();
        }
        return hourText;
    }

    private boolean isSameMonth(String date1, String date2) {
        return date1.split("/")[0].equals(date2.split("/")[0]);
    }

    private boolean isSameYear(String date1, String date2) {
        return date1.split("/")[2].equals(date2.split("/")[2]);
    }

}

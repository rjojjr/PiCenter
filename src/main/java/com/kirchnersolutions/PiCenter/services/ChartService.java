package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.ChartResponse;
import com.kirchnersolutions.PiCenter.servers.beans.DiffInterval;
import com.kirchnersolutions.PiCenter.servers.beans.TempInterval;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.hibernate.tool.schema.extract.spi.ForeignKeyInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static com.kirchnersolutions.utilities.CalenderConverter.getDaysBetween;
import static com.kirchnersolutions.utilities.CalenderConverter.getDaysInMonth;

import static com.kirchnersolutions.utilities.CalenderConverter.DAY;

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

    public ChartResponse getDiffChartData(ChartRequest chartRequest) throws Exception{
        return new ChartResponse(generateDiffChartData(chartRequest));
    }

    /**
     *
     * @param chartRequest
     * @return
     */
    private TempInterval[] generateChartData(ChartRequest chartRequest) throws Exception{
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
        TempInterval[] tempIntervalList = new TempInterval[intervals.size()];
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
            tempIntervalList[count] = new TempInterval(getAverageIntervalString(intervals.get(count), true), br, lr, sr, of, ou, 0);
            count++;
        }
        return tempIntervalList;
    }

    DiffInterval[] generateDiffChartData(ChartRequest chartRequest) throws Exception{
        String start = chartRequest.getFromDate();
        String end = chartRequest.getToDate();
        Future<List<String[]>>[] futures = new Future[5];
        futures[0] = threadPoolTaskExecutor.submit(new DiffChartValuesThread(start, end, "bedroom"));
        futures[1] = threadPoolTaskExecutor.submit(new DiffChartValuesThread(start, end, "livingroom"));
        futures[2] = threadPoolTaskExecutor.submit(new DiffChartValuesThread(start, end, "serverroom"));
        futures[3] = threadPoolTaskExecutor.submit(new DiffChartValuesThread(start, end, "office"));
        futures[4] = threadPoolTaskExecutor.submit(new DiffChartValuesThread(start, end, "outside"));
        List<String[]> bedroomValues = futures[0].get();
        List<String[]> livingroomValues = futures[1].get();
        List<String[]> serverroomValues = futures[2].get();
        List<String[]> officeValues = futures[3].get();
        List<String[]> outsideValues = futures[4].get();
        DiffInterval[] diffIntervalList = new DiffInterval[bedroomValues.size()];
        int type = 0, startLoop = 0, endLoop = bedroomValues.size() - 1;
        if(chartRequest.getType().contains("hum")){
            type = 1;
        }
        if(bedroomValues.size() == 1){
            String br = "0-0", lr = "0-0", sr = "0-0", of = "0-0", ou = "0-0";
            //Check for nulls
                if (bedroomValues.get(0) != null) {
                    br = bedroomValues.get(0)[type];
                }
                if (livingroomValues.get(0) != null) {
                    lr = livingroomValues.get(0)[type];
                }
                if (serverroomValues.get(0) != null) {
                    sr = serverroomValues.get(0)[type];
                }
                if (officeValues.get(0) != null) {
                    of = officeValues.get(0)[type];
                }
                if (outsideValues.get(0) != null) {
                    ou = outsideValues.get(0)[type];
                }
            diffIntervalList = new DiffInterval[bedroomValues.size() + 1];
            diffIntervalList[0] = new DiffInterval(getDiffIntervalString(start, 0),
                    new double[]{Double.parseDouble(br.split("-")[0]), Double.parseDouble(br.split("-")[1])},
                    new double[]{Double.parseDouble(lr.split("-")[0]), Double.parseDouble(lr.split("-")[1])},
                    new double[]{Double.parseDouble(sr.split("-")[0]), Double.parseDouble(sr.split("-")[1])},
                    new double[]{Double.parseDouble(of.split("-")[0]), Double.parseDouble(of.split("-")[1])},
                    new double[]{Double.parseDouble(ou.split("-")[0]), Double.parseDouble(ou.split("-")[1])},
                    new double[]{0, 0});
            diffIntervalList[1] = new DiffInterval(getDiffIntervalString(start, 0),
                    new double[]{Double.parseDouble(br.split("-")[0]), Double.parseDouble(br.split("-")[1])},
                    new double[]{Double.parseDouble(lr.split("-")[0]), Double.parseDouble(lr.split("-")[1])},
                    new double[]{Double.parseDouble(sr.split("-")[0]), Double.parseDouble(sr.split("-")[1])},
                    new double[]{Double.parseDouble(of.split("-")[0]), Double.parseDouble(of.split("-")[1])},
                    new double[]{Double.parseDouble(ou.split("-")[0]), Double.parseDouble(ou.split("-")[1])},
                    new double[]{0, 0});
        }else{
            String br = "0-0", lr = "0-0", sr = "0-0", of = "0-0", ou = "0-0";
            //Check for nulls
            for(int count = 0; count < bedroomValues.size(); count++){
                if (bedroomValues.get(count) != null) {
                    br = bedroomValues.get(count)[type];
                }
                if (livingroomValues.get(count) != null) {
                    lr = livingroomValues.get(count)[type];
                }
                if (serverroomValues.get(count) != null) {
                    sr = serverroomValues.get(count)[type];
                }
                if (officeValues.get(count) != null) {
                    of = officeValues.get(count)[type];
                }
                if (outsideValues.get(count) != null) {
                    ou = outsideValues.get(count)[type];
                }
                diffIntervalList[count] = new DiffInterval(getDiffIntervalString(start, count),
                        new double[]{Double.parseDouble(br.split("-")[0]), Double.parseDouble(br.split("-")[1])},
                        new double[]{Double.parseDouble(lr.split("-")[0]), Double.parseDouble(lr.split("-")[1])},
                        new double[]{Double.parseDouble(sr.split("-")[0]), Double.parseDouble(sr.split("-")[1])},
                        new double[]{Double.parseDouble(of.split("-")[0]), Double.parseDouble(of.split("-")[1])},
                        new double[]{Double.parseDouble(ou.split("-")[0]), Double.parseDouble(ou.split("-")[1])},
                        new double[]{0, 0});
            }
        }

        return diffIntervalList;
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

    private class DiffChartValuesThread implements Callable<List<String[]>>{

        private String start, end;
        private String room;

        public DiffChartValuesThread(String start, String end, String room){
            this.start = start;
            this.end = end;
            this.room = room;
        }

        public List<String[]> call(){
            return getHighLow(start, end, room);
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
     * Get highs and lows for each day inclusively between start and end.
     * @param start
     * @param end
     * @param room
     * @return
     */
    List<String[]> getHighLow(String start, String end, String room){
        if (CalenderConverter.getMillisFromDateString(start, "/") > CalenderConverter.getMillisFromDateString(end, "/")) {
            return getHighLow(end, start, room);
        }
        List<String[]> intervals = new ArrayList<>();
        long endMillis = CalenderConverter.getMillisFromDateString(end, "/") + CalenderConverter.DAY;
        long startMillis = CalenderConverter.getMillisFromDateString(start, "/");
        if (start.equals(end)) {
            String[] date = start.split("/");
            intervals.add(statService.getHighLow(start, room));
            return intervals;
        } else if (isSameMonth(start, end) && isSameYear(start, end)) {
            String[] date = start.split("/");
            for (int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++) {
                intervals.add(statService.getHighLow(date[0] + "/" + k + "/" + date[2], room));
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
                    for (int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++) {
                        intervals.add(statService.getHighLow(j + "/" + k + "/" + date[2], room));
                    }
                } else {
                    for (int k = startDay; k <= getDaysInMonth(j, Integer.parseInt(date[2])); k++) {
                        intervals.add(statService.getHighLow(j + "/" + k + "/" + date[2], room));
                    }
                }
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
                        for (int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++) {
                            intervals.add(statService.getHighLow(j + "/" + k + "/" + u, room));
                        }
                    } else {
                        for (int k = startDay; k <= getDaysInMonth(j, u); k++) {
                            intervals.add(statService.getHighLow(j + "/" + k + "/" + u, room));
                        }
                    }
                }
            }
            return intervals;
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
                        for (int k = startDay; k <= getDaysInMonth(j, u); k++) {
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

    String getDiffIntervalString(String startDate, int count){
        long time = CalenderConverter.getMillisFromDateString(startDate, "/") + ((500 + CalenderConverter.DAY) * count);
        return CalenderConverter.getMonthDayYear(time, "/", ":");
    }

    String getAverageIntervalString(long time, boolean withDate){
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

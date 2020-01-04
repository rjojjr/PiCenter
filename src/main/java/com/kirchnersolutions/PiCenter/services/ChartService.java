package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.servers.beans.*;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    public ChartService(@Lazy StatService statService, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.statService = statService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public ChartResponse getChartData(ChartRequest chartRequest) throws Exception{
        return new ChartResponse(generateChartData(chartRequest));
    }

    public ChartResponse getDiffChartData(ChartRequest chartRequest) throws Exception{
        return new ChartResponse(generateDiffChartData(chartRequest));
    }

    public ChartResponse getScatChartData(ChartRequest chartRequest) throws Exception{
        return new ChartResponse(generateScatterData(chartRequest));
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
        List<Long> intervals = SharedLogic.getTimeIntervals(start, end, interval);
        Future<List<double[]>>[] futures = new Future[5];
        futures[2] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "bedroom"));
        futures[1] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "livingroom"));
        futures[3] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "serverroom"));
        futures[0] = threadPoolTaskExecutor.submit(new ChartValuesThread(intervals, interval, "office"));
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
            if(interval == 1){
                tempIntervalList[count] = new TempInterval(getAverageIntervalString(intervals.get(count), false), br, lr, sr, of, ou, 0);
            }else{
                tempIntervalList[count] = new TempInterval(getAverageIntervalString(intervals.get(count), true), br, lr, sr, of, ou, 0);
            }
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

    ScatterInterval[] generateScatterData(ChartRequest chartRequest) throws Exception {
        String start = chartRequest.getFromDate();
        String end = chartRequest.getToDate();
        String type = chartRequest.getType();
        Future<ScatterPoint[]>[] futures = new Future[4];
        futures[0] = threadPoolTaskExecutor.submit(() -> {
            return statService.generateRoomScatterPoints(start, end, "office", type);
        });
        futures[1] = threadPoolTaskExecutor.submit(() -> {
            return statService.generateRoomScatterPoints(start, end, "livingroom", type);
        });
        futures[2] = threadPoolTaskExecutor.submit(() -> {
            return statService.generateRoomScatterPoints(start, end, "bedroom", type);
        });
        futures[3] = threadPoolTaskExecutor.submit(() -> {
            return statService.generateRoomScatterPoints(start, end, "serverroom", type);
        });
        ScatterInterval[] intervals = new ScatterInterval[4];
        intervals[0] = new ScatterInterval(futures[0].get());
        intervals[1] = new ScatterInterval(futures[1].get());
        intervals[2] = new ScatterInterval(futures[2].get());
        intervals[3] = new ScatterInterval(futures[3].get());
        return intervals;
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
            return getChartValues(SharedLogic.generateIntervalWindows(intervals, interval), room);
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
     * Generates interval based on time between arguments.
     * @param start
     * @param end
     * @return
     */
    private int getInterval(String start, String end) {
        if (start.equals(end)) {
            return 1;
        } else if (SharedLogic.isSameMonth(start, end) && SharedLogic.isSameYear(start, end)) {
            int dif = Integer.parseInt(end.split("/")[1]) - Integer.parseInt(start.split("/")[1]);
            if (dif <= 3) {
                return 3;
            }
            if (dif <= 6) {
                return 6;
            }
            if (dif <= 8) {
                return 12;
            }
            return 24;
        } else {
            int dif = getDaysBetween(start, end, "/");
            if (dif <= 3) {
                return 3;
            }
            if (dif <= 6) {
                return 6;
            }
            if (dif <= 8) {
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
        } else if (SharedLogic.isSameMonth(start, end) && SharedLogic.isSameYear(start, end)) {
            String[] date = start.split("/");
            for (int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++) {
                intervals.add(statService.getHighLow(date[0] + "/" + k + "/" + date[2], room));
            }
            return intervals;
        } else if (SharedLogic.isSameYear(start, end)) {
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
                System.out.println("" + u);
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
                hourText = "12AM";
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

}

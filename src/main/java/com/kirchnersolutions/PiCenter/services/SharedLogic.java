package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.utilities.CalenderConverter;

import java.util.ArrayList;
import java.util.List;

import static com.kirchnersolutions.utilities.CalenderConverter.getDaysInMonth;

public class SharedLogic {
    /**
     * Generates list of periods to average chart points from.
     * @param chartIntervals
     * @param interval
     * @return
     */
    static List<long[]> generateIntervalWindows(List<Long> chartIntervals, int interval){
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
     * Get list of millis representing each interval between start and end.
     *
     * @param start:    MM/DD/YYYY
     * @param end:      MM/DD/YYYY
     * @param interval: n hours
     * @return
     */
    static List<Long> getTimeIntervals(String start, String end, int interval) {
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

    static boolean isSameMonth(String date1, String date2) {
        return date1.split("/")[0].equals(date2.split("/")[0]);
    }

    static boolean isSameYear(String date1, String date2) {
        return date1.split("/")[2].equals(date2.split("/")[2]);
    }
}

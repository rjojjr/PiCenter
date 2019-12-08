package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.Interval;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.kirchnersolutions.utilities.CalenderConverter.getDaysBetween;
import static com.kirchnersolutions.utilities.CalenderConverter.getDaysInMonth;

@Service
public class ChartService {

    private StatService statService;

    @Autowired
    public ChartService(StatService statService) {
        this.statService = statService;
    }

    private Interval[] getChartData(ChartRequest chartRequest) {
        String start = chartRequest.getFromDate();
        String end = chartRequest.getToDate();
        int interval = getInterval(start, end);
        List<Long> intervals = getTimeIntervals(start, end, interval);
        return null;
    }

    /**
     * Generates list of periods to average chart points from.
     * @param chartIntervals
     * @param interval
     * @return
     */
    private List<long[]> generateAverageIntervals(List<Long> chartIntervals, int interval){
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
        List<Long> intervals = new ArrayList<>();
        long endMillis = CalenderConverter.getMillisFromDateString(end, "/") + CalenderConverter.DAY;
        long startMillis = CalenderConverter.getMillisFromDateString(start, "/");
        if (start.equals(end)) {
            String[] date = start.split("/");
            if (interval == 24) {
                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 12, 0, 0));
            } else {
                for (int i = 0; i <= 23; i += interval) {
                    intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
                }
            }
            return intervals;
        } else if (isSameMonth(start, end) && isSameYear(start, end)) {
            String[] date = start.split("/");
            for (int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++) {
                if (interval == 24) {
                    intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 12, 0, 0));
                } else {
                    for (int i = 0; i <= 23; i += interval) {
                        intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
                    }
                }
            }
            return intervals;
        } else if (isSameYear(start, end)) {
            int endMonth = Integer.parseInt(end.split("/")[0]);
            int startMonth = Integer.parseInt(start.split("/")[0]);
            String[] date = start.split("/");
            for (int j = startMonth; j <= endMonth; j++) {
                if (j == endMillis) {
                    for (int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++) {
                        if (interval == 24) {
                            intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 12, 0, 0));
                        } else {
                            for (int i = 0; i <= 23; i += interval) {
                                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
                            }
                        }
                    }
                } else {
                    for (int k = Integer.parseInt(date[1]); k <= getDaysInMonth(j, Integer.parseInt(date[2])); k++) {
                        if (interval == 24) {
                            intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 12, 0, 0));
                        } else {
                            for (int i = 0; i <= 23; i += interval) {
                                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
                            }
                        }
                    }
                }
            }
            return intervals;
        } else if (Integer.parseInt(start.split("/")[2]) > Integer.parseInt(end.split("/")[2])) {
            return null;
        } else {
            String[] date = start.split("/");
            int endMonth = Integer.parseInt(end.split("/")[0]);
            int startMonth = Integer.parseInt(start.split("/")[0]);
            int endYear = Integer.parseInt(end.split("/")[2]);
            int startYear = Integer.parseInt(start.split("/")[2]);
            for (int u = startYear; u <= endYear; u++) {
                for (int j = startMonth; j <= endMonth; j++) {
                    if (j == endMillis) {
                        for (int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++) {
                            if (interval == 24) {
                                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 12, 0, 0));
                            } else {
                                for (int i = 0; i <= 23; i += interval) {
                                    if (i == 24) {
                                        intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 23, 50, 0));
                                    } else {
                                        intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
                                    }
                                }
                            }
                        }
                    } else {
                        for (int k = Integer.parseInt(date[1]); k <= getDaysInMonth(j, Integer.parseInt(date[2])); k++) {
                            if (interval == 24) {
                                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 12, 0, 0));
                            } else {
                                for (int i = 0; i <= 23; i += interval) {
                                    if (i == 24) {
                                        intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), 23, 50, 0));
                                    } else {
                                        intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return intervals;
        }
    }

    private boolean isSameMonth(String date1, String date2) {
        return date1.split("/")[0].equals(date2.split("/")[0]);
    }

    private boolean isSameYear(String date1, String date2) {
        return date1.split("/")[2].equals(date2.split("/")[2]);
    }

}

package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.Interval;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.InitBinderDataBinderFactory;

import java.util.ArrayList;
import java.util.List;

import static com.kirchnersolutions.utilities.CalenderConverter.getDaysInMonth;

@Service
public class ChartService {

    private StatService statService;

    @Autowired
    public ChartService(StatService statService){
        this.statService = statService;
    }

    private Interval[] getChartData(ChartRequest chartRequest){
        int daysBetween = CalenderConverter.getDaysBetween(chartRequest.getFromDate(), chartRequest.getToDate(), "/");
        if(daysBetween < 2){

        }
        return null;
    }

    /**
     * Get list of millis representing each interval between start and end.
     * @param start: MM/DD/YYYY
     * @param end: MM/DD/YYYY
     * @param interval: n hours
     * @return
     */
    private List<Long> getTimeIntervals(String start, String end, int interval){
        List<Long> intervals = new ArrayList<>();
        long endMillis = CalenderConverter.getMillisFromDateString(end, "/") + CalenderConverter.DAY;
        long startMillis = CalenderConverter.getMillisFromDateString(start, "/");
        if(start.equals(end)){
            String[] date = start.split("/");
            for(int i = 0; i < 24; i+= interval){
                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
            }
            return intervals;
        }else if(isSameMonth(start, end) && isSameYear(start, end)){
            String[] date = start.split("/");
            for(int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++){
                for(int i = 0; i < 24; i+= interval){
                    intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]), k, Integer.parseInt(date[2]), i, 0, 0));
                }
            }
            return intervals;
        }else if(isSameYear(start, end)){
            int endMonth = Integer.parseInt(end.split("/")[0]);
            int startMonth = Integer.parseInt(start.split("/")[0]);
            String[] date = start.split("/");
            for(int j = startMonth; j <= endMonth; j++){
                if(j == endMillis){
                    for(int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++){
                        for(int i = 0; i < 24; i+= interval){
                            intervals.add(CalenderConverter.getMillis(j, k, Integer.parseInt(date[2]), i, 0, 0));
                        }
                    }
                }else {
                    for(int k = Integer.parseInt(date[1]); k <= getDaysInMonth(j, Integer.parseInt(date[2])); k++){
                        for(int i = 0; i < 24; i+= interval){
                            intervals.add(CalenderConverter.getMillis(j, k, Integer.parseInt(date[2]), i, 0, 0));
                        }
                    }
                }
            }
            return intervals;
        }else if(Integer.parseInt(start.split("/")[2]) > Integer.parseInt(end.split("/")[2])){
            return null;
        }else{
            String[] date = start.split("/");
            int endMonth = Integer.parseInt(end.split("/")[0]);
            int startMonth = Integer.parseInt(start.split("/")[0]);
            int endYear = Integer.parseInt(end.split("/")[2]);
            int startYear = Integer.parseInt(start.split("/")[2]);
            for(int u = startYear; u <= endYear; u++){
                for(int j = startMonth; j <= endMonth; j++){
                    if(j == endMillis){
                        for(int k = Integer.parseInt(date[1]); k <= Integer.parseInt(end.split("/")[1]); k++){
                            for(int i = 0; i < 24; i+= interval){
                                intervals.add(CalenderConverter.getMillis(j, k, u, i, 0, 0));
                            }
                        }
                    }else {
                        for(int k = Integer.parseInt(date[1]); k <= getDaysInMonth(j, Integer.parseInt(date[2])); k++){
                            for(int i = 0; i < 24; i+= interval){
                                intervals.add(CalenderConverter.getMillis(j, k, u, i, 0, 0));
                            }
                        }
                    }
                }
            }
            return intervals;
        }
    }

    private boolean isSameMonth(String date1, String date2){
        return date1.split("/")[0].equals(date2.split("/")[0]);
    }

    private boolean isSameYear(String date1, String date2){
        return date1.split("/")[2].equals(date2.split("/")[2]);
    }

}

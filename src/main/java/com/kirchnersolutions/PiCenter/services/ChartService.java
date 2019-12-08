package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.Interval;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.InitBinderDataBinderFactory;

import java.util.ArrayList;
import java.util.List;

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
                intervals.add(CalenderConverter.getMillis(Integer.parseInt(date[0]) + 1, Integer.parseInt(date[1]), Integer.parseInt(date[2]), i, 0, 0));
            }
            return intervals;
        }else if(isSameMonth(start, end) && isSameYear(start, end)){
            
        }
    }

    private boolean isSameMonth(String date1, String date2){
        return date1.split("/")[0].equals(date2.split("/")[0]);
    }

    private boolean isSameYear(String date1, String date2){
        return date1.split("/")[2].equals(date2.split("/")[2]);
    }

}

package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.Interval;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.InitBinderDataBinderFactory;

@Service
public class ChartService {


    private Interval[] getChartData(ChartRequest chartRequest){
        int daysBetween = CalenderConverter.getDaysBetween(chartRequest.getFromDate(), chartRequest.getToDate(), "/");
        return null;
    }

}

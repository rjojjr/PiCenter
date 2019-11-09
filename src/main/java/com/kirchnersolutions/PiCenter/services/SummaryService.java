package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.RoomSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.constants.StatConstants;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SummaryService {

    @Autowired
    private ReadingRepository readingRepository;

    private RoomSummary getRoomSummary(String roomName){
        Long time = System.currentTimeMillis();
        String[] temp = new String[6];
        String[] humidity = new String[6];
        List<Reading> reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.HOUR, time, roomName);
    }

    private String getTempMean(List<Reading> readings, int precision){
        BigDecimal mean = new BigDecimal(0);
        int count = 0;
        for(Reading reading : readings){
            BigDecimal temp = new BigDecimal(reading.getTemp());
            mean = mean.add(temp);
            count++;
        }
        return findMean(mean, count, precision);
    }

    private String getHumidityMean(List<Reading> readings, int precision){
        BigDecimal mean = new BigDecimal(0);
        int count = 0;
        for(Reading reading : readings){
            BigDecimal temp = new BigDecimal(reading.getHumidity());
            mean = mean.add(temp);
            count++;
        }
        return findMean(mean, count, precision);
    }

    private String findMean(BigDecimal sum, int count, int precision){
        sum = sum.divide(new BigDecimal(count));
        String temp = sum.toString();
        char[] decimals = temp.split(".")[1].toCharArray();
        if(decimals.length > precision){
            String part = "";
            for(int i = 0; i < precision; i++){
                part+= decimals[i];
            }
            return temp.split(".")[0] + "." + part;
        }
        if(decimals.length < precision){
            String zeros = "";
            for(int i = decimals.length; i <= precision; i++){
                zeros+= "0";
            }
            return temp + zeros;
        }
        return temp;
    }

}

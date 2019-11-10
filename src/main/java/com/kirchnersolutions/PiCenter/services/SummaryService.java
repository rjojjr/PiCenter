package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.RoomSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.constants.StatConstants;

import java.math.BigDecimal;
import java.util.List;

@DependsOn({"debuggingService", "appUserRepository", "readingRepository"})
@Service
public class SummaryService {

    @Autowired
    private ReadingRepository readingRepository;

    public RoomSummary[] getRoomSummaries(int precision){
        String[] rooms = RoomConstants.rooms;
        RoomSummary[] summaries = new RoomSummary[rooms.length];
        int count = 0;
        for(String room : rooms){
            summaries[count] = getRoomSummary(room, precision);
            count++;
        }
        return summaries;
    }

    private RoomSummary getRoomSummary(String roomName, int precision){
        Long time = System.currentTimeMillis();
        String[] temp = new String[7];
        String[] humidity = new String[7];
        List<Reading> reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.HOUR, time, roomName);
        if(precision > 0){
            temp[0] = reads.get(0).getTemp() + "." + getZeros(precision);
            humidity[0] = reads.get(0).getHumidity() + "." + getZeros(precision);
        }else {
            temp[0] = reads.get(0).getTemp() + "";
            humidity[0] = reads.get(0).getHumidity() + "";
        }
        temp[1] = getTempMean(reads, precision);
        humidity[1] = getHumidityMean(reads, precision);
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.TWO_HOUR, time, roomName);
        temp[2] = getTempMean(reads, precision);
        humidity[2] = getHumidityMean(reads, precision);
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.THREE_HOUR, time, roomName);
        temp[3] = getTempMean(reads, precision);
        humidity[3] = getHumidityMean(reads, precision);
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.SIX_HOUR, time, roomName);
        temp[4] = getTempMean(reads, precision);
        humidity[4] = getHumidityMean(reads, precision);
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.TWELVE_HOUR, time, roomName);
        temp[5] = getTempMean(reads, precision);
        humidity[5] = getHumidityMean(reads, precision);
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - (2 * StatConstants.TWELVE_HOUR ), time, roomName);
        temp[6] = getTempMean(reads, precision);
        humidity[6] = getHumidityMean(reads, precision);
        return new RoomSummary(roomName, temp, humidity);
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

    private static String getZeros(int howMany){
        if(howMany == 0) return "";
        String zeros = "";
        for(int i = 0; i < howMany; i++){
            zeros = zeros + "0";
        }
        return zeros;
    }

    private String findMean(BigDecimal sum, int count, int precision){
        sum = sum.divide(new BigDecimal(count));
        String temp = sum.toString();
        if(temp.split(".").length > 1){
            char[] decimals = temp.split(".")[1].toCharArray();
            if(decimals.length > precision){
                String part = "";
                for(int i = 0; i < precision; i++){
                    part = part + decimals[i];
                }
                return temp.split(".")[0] + "." + part;
            }
            if(decimals.length < precision){
                return temp + getZeros(precision - decimals.length);
            }
            return temp;
        }
        if(precision > 0){
            return temp + "." + getZeros(precision);
        }
        return temp;
    }
}

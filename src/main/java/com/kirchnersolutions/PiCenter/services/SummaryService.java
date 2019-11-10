package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.RoomSummary;
import com.kirchnersolutions.utilities.BigDecimalMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.constants.StatConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        String[] tempDevi = new String[7];
        String[] humidityDevi = new String[7];
        List<Reading> reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.HOUR, time, roomName);
        if(precision > 0){
            temp[0] = reads.get(0).getTemp() + "." + getZeros(precision);
            humidity[0] = reads.get(0).getHumidity() + "." + getZeros(precision);
            tempDevi[0] = "0" + "." + getZeros(precision);
            humidityDevi[0] = "0" + "." + getZeros(precision);
        }else {
            temp[0] = reads.get(0).getTemp() + "";
            humidity[0] = reads.get(0).getHumidity() + "";
            tempDevi[0] = "0";
            humidityDevi[0] = "0";
        }
        String[] stats = getTempStats(reads, precision);
        temp[1] = stats[0];
        tempDevi[1] = stats[1];
        stats = getHumidityStats(reads, precision);
        humidity[1] = stats[0];
        humidityDevi[1] = stats[1];
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.TWO_HOUR, time, roomName);
        stats = getTempStats(reads, precision);
        temp[2] = stats[0];
        tempDevi[2] = stats[1];
        stats = getHumidityStats(reads, precision);
        humidity[2] = stats[0];
        humidityDevi[2] = stats[1];
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.THREE_HOUR, time, roomName);
        stats = getTempStats(reads, precision);
        temp[3] = stats[0];
        tempDevi[3] = stats[1];
        stats = getHumidityStats(reads, precision);
        humidity[3] = stats[0];
        humidityDevi[3] = stats[1];
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.SIX_HOUR, time, roomName);
        stats = getTempStats(reads, precision);
        temp[4] = stats[0];
        tempDevi[4] = stats[1];
        stats = getHumidityStats(reads, precision);
        humidity[4] = stats[0];
        humidityDevi[4] = stats[1];
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.TWELVE_HOUR, time, roomName);
        stats = getTempStats(reads, precision);
        temp[5] = stats[0];
        tempDevi[5] = stats[1];
        stats = getHumidityStats(reads, precision);
        humidity[5] = stats[0];
        humidityDevi[5] = stats[1];
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - (2 * StatConstants.TWELVE_HOUR ), time, roomName);
        stats = getTempStats(reads, precision);
        temp[6] = stats[0];
        tempDevi[6] = stats[1];
        stats = getHumidityStats(reads, precision);
        humidity[6] = stats[0];
        humidityDevi[6] = stats[1];
        return new RoomSummary(roomName, temp, humidity, tempDevi, humidityDevi);
    }

    private String[] getTempStats(List<Reading> readings, int precision){
        BigDecimal sum = new BigDecimal(0);
        int count = 0;
        for(Reading reading : readings){
            BigDecimal temp = new BigDecimal(reading.getTemp());
            sum = sum.add(temp);
            count++;
        }
        return findStats(RoomConstants.READING_TYPE_TEMP, sum, precision, readings, true);
    }

    private String[] getHumidityStats(List<Reading> readings, int precision){
        BigDecimal sum = new BigDecimal(0);
        int count = 0;
        for(Reading reading : readings){
            BigDecimal temp = new BigDecimal(reading.getHumidity());
            sum = sum.add(temp);
            count++;
        }
        return findStats(RoomConstants.READING_TYPE_HUMIDITY, sum, precision, readings, true);
    }

    private static String getZeros(int howMany){
        if(howMany == 0) return "";
        String zeros = "";
        for(int i = 0; i < howMany; i++){
            zeros = zeros + "0";
        }
        return zeros;
    }

    private static String round(String value, int precision){
        if(value.split("\\.").length > 1){
            char[] decimals = value.split("\\.")[1].toCharArray();
            if(decimals.length > precision){
                String part = "";
                for(int i = 0; i < precision; i++){
                    part = part + decimals[i];
                }
                return value.split("\\.")[0] + "." + part;
            }
            if(decimals.length < precision){
                return value + getZeros(precision - decimals.length);
            }
            return value;
        }
        if(precision > 0){
            return value + "." + getZeros(precision);
        }
        return value;
    }

    private String[] findStats(int readingType, BigDecimal sum, int precision, List<Reading> readings, boolean sample){
        //0 = mean, 1 = standard deviation
        String[] results = new String[2];
        BigDecimal mean = new BigDecimal(findMean(sum, readings.size()));
        results[0] = round(mean.toString(), precision);
        BigDecimal deviation = findStandardDeviation(readingType, mean, sample, readings, precision);
        results[1] = round(deviation.toString(), precision);
        return results;
    }

    private BigDecimal findStandardDeviation(int readingType, BigDecimal mean, boolean sample, List<Reading> readings, int precision){
        List<BigDecimal> squares = new ArrayList<>();
        for(Reading reading : readings){
            BigDecimal read;
            if(readingType == RoomConstants.READING_TYPE_TEMP){
                read = new BigDecimal(reading.getTemp());
            }else {
                read = new BigDecimal(reading.getHumidity());
            }
            read = read.subtract(mean);
            squares.add(read.multiply(read));
        }
        BigDecimal sum = new BigDecimal("0");
        for(BigDecimal square : squares){
            sum.add(square);
        }
        BigDecimal square;
        if(sample){
            square = new BigDecimal(findMean(sum, squares.size() - 1));
        }else {
            square = new BigDecimal(findMean(sum, squares.size()));
        }
        square = BigDecimalMath.bigSqrt(square);
        return square;
    }

    private String findMean(BigDecimal sum, int count){
        sum = sum.divide(new BigDecimal(count));
        return sum.toString();
    }
}

package com.kirchnersolutions.PiCenter.services;

/**
 * PiCenter: Raspberry Pi home automation control center.
 *
 * Copyright (C) 2019  Robert Kirchner JR
 *
 *         This program is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU Affero General Public License as
 *         published by the Free Software Foundation, either version 3 of the
 *         License, or (at your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU Affero General Public License for more details.
 *
 *         You should have received a copy of the GNU Affero General Public License
 *         along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.RoomSummary;
import com.kirchnersolutions.utilities.BigDecimalMath;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.constants.StatConstants;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@DependsOn({"debuggingService", "appUserRepository", "readingRepository"})
@Service
public class StatService {

    private ReadingRepository readingRepository;

    @Autowired
    public StatService(ReadingRepository readingRepository){
        this.readingRepository = readingRepository;
    }

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

    /**
     * Returns array of temp and humidity average of readings between time +- window in room room.
     * @param time
     * @param window
     * @param room
     * @return
     */
    double[] getAverage(long time, long window, String room){
        long startTime = time - window;
        long endTime = time + window;
        List<Reading> readings = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(startTime, endTime, room);
        return getMeans(getSums(readings));
    }

    private double[] getMeans(SumBean sums){
        double[] means = new double[2];
        means[0] = Double.parseDouble(round(findMean(sums.getSums()[0], sums.getCount()), 1));
        means[1] = Double.parseDouble(round(findMean(sums.getSums()[1], sums.getCount()), 1));
        return means;
    }

    private SumBean getSums(List<Reading> readings){
        int count = 0;
        BigDecimal temp = new BigDecimal("0");
        BigDecimal humidity = new BigDecimal("0");
        for(Reading reading : readings){
            temp = temp.add(new BigDecimal(reading.getTemp()));
            humidity = humidity.add(new BigDecimal(reading.getHumidity()));
            count++;
        }
        BigDecimal[] sums = {temp, humidity};
        return new SumBean(sums, count);
    }

    private RoomSummary getRoomSummary(String roomName, int precision){
        Long time = System.currentTimeMillis();
        String[] temp = new String[7];
        String[] humidity = new String[7];
        String[] tempDevi = new String[7];
        String[] humidityDevi = new String[7];
        String[] stats = new String[2];
        List<Reading> reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.HOUR, time, roomName);
        if(reads.isEmpty()){
            for(int i = 0; i < 2; i++){
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        }else{
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
            stats = getTempStats(reads, precision);
            temp[1] = stats[0];
            tempDevi[1] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[1] = stats[0];
            humidityDevi[1] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.TWO_HOUR, time, roomName);
        if(reads.isEmpty()){
            for(int i = 2; i < 3; i++){
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        }else {
            stats = getTempStats(reads, precision);
            temp[2] = stats[0];
            tempDevi[2] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[2] = stats[0];
            humidityDevi[2] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.THREE_HOUR, time, roomName);
        if(reads.isEmpty()){
            for(int i = 3; i < 4; i++){
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        }else {
            stats = getTempStats(reads, precision);
            temp[3] = stats[0];
            tempDevi[3] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[3] = stats[0];
            humidityDevi[3] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.SIX_HOUR, time, roomName);
        if(reads.isEmpty()){
            for(int i = 4; i < 5; i++){
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        }else {
            stats = getTempStats(reads, precision);
            temp[4] = stats[0];
            tempDevi[4] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[4] = stats[0];
            humidityDevi[4] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.TWELVE_HOUR, time, roomName);
        if(reads.isEmpty()){
            for(int i = 5; i < 6; i++){
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        }else {
            stats = getTempStats(reads, precision);
            temp[5] = stats[0];
            tempDevi[5] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[5] = stats[0];
            humidityDevi[5] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - (2 * StatConstants.TWELVE_HOUR ), time, roomName);
        if(reads.isEmpty()){
            for(int i = 5; i < 6; i++){
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        }else {
            stats = getTempStats(reads, precision);
            temp[6] = stats[0];
            tempDevi[6] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[6] = stats[0];
            humidityDevi[6] = stats[1];
        }
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
            read = new BigDecimal(read.doubleValue() - mean.doubleValue());
            squares.add(new BigDecimal(read.doubleValue() * read.doubleValue()));
        }
        BigDecimal sum = new BigDecimal("0");
        for(BigDecimal square : squares){
            sum = sum.add(square);
        }
        BigDecimal square;
        if(sample){
            square = new BigDecimal(findMean(sum, squares.size() - 1));
        }else {
            square = new BigDecimal(findMean(sum, squares.size()));
        }
        square = BigDecimalMath.sqrt(square, 100);
        return square;
    }

    private String findMean(BigDecimal sum, int count){
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        if(count == 0){
            return "0.00";
        }
        sum = sum.divide(new BigDecimal(count), mc);
        return sum.toString();
    }

    @Data
    @AllArgsConstructor
    private class SumBean {
        BigDecimal[] sums = new BigDecimal[2];
        int count = 0;
    }
}

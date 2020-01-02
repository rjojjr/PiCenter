package com.kirchnersolutions.PiCenter.services;

/**
 * PiCenter: Raspberry Pi home automation control center.
 * <p>
 * Copyright (C) 2019  Robert Kirchner JR
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.RoomSummary;
import com.kirchnersolutions.PiCenter.servers.beans.ScatterInterval;
import com.kirchnersolutions.PiCenter.servers.beans.ScatterPoint;
import com.kirchnersolutions.utilities.BigDecimalMath;
import com.kirchnersolutions.utilities.ByteTools;
import com.kirchnersolutions.utilities.CalenderConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.constants.StatConstants;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.kirchnersolutions.utilities.CalenderConverter.*;
import static java.lang.Math.abs;

@DependsOn({"debuggingService", "appUserRepository", "readingRepository", "chartService"})
@Service
public class StatService {

    private ReadingRepository readingRepository;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private DebuggingService debuggingService;
    private PearsonCorrelation pearsonCorrelation;

    @Autowired
    public StatService(ReadingRepository readingRepository, ThreadPoolTaskExecutor taskExecutor, DebuggingService debuggingService, @Lazy PearsonCorrelation pearsonCorrelation) {
        this.readingRepository = readingRepository;
        this.threadPoolTaskExecutor = taskExecutor;
        this.debuggingService = debuggingService;
        this.pearsonCorrelation = pearsonCorrelation;
    }


    public RoomSummary[] getRoomSummaries(int precision) throws Exception {
        String[] rooms = RoomConstants.rooms;
        List<double[]> curRelation = getRelationFromDir(new File("PiCenter/Learning/Daily/Pearson"));
        List<double[]> longRelation = getRelationFromDir(new File("PiCenter/Learning/LongTerm/Pearson"));
        List<double[]> change = getRelationFromDir(new File("PiCenter/Learning/Daily/Change"));
        List<double[]> longChange = getRelationFromDir(new File("PiCenter/Learning/LongTerm/Change"));
        if(curRelation.isEmpty()){
            for(int i = 0; i < 5; i++ ){
                double[] empty = {0.0,0.0};
                curRelation.add(empty);
            }
        }
        if(longRelation.isEmpty()){
            for(int i = 0; i < 5; i++ ){
                double[] empty = {0.0,0.0};
                longRelation.add(empty);
            }
        }
        if(change.isEmpty()){
            for(int i = 0; i < 5; i++ ){
                double[] empty = {0.0,0.0};
                change.add(empty);
            }
        }
        if(longChange.isEmpty()){
            for(int i = 0; i < 5; i++ ){
                double[] empty = {0.0,0.0};
                longChange.add(empty);
            }
        }
        RoomSummary[] summaries = new RoomSummary[rooms.length];
        Future<RoomSummary>[] futures = new Future[rooms.length];
        int count = 0;
        for (String room : rooms) {
            futures[count] = threadPoolTaskExecutor.submit(() -> getRoomSummary(room, precision));
            count++;
        }
        double[] empty = {1.000, 1.000};
        count = 0;
        for (String room : rooms) {
            summaries[count] = futures[count].get();
            if(count < 4) {
                if (curRelation.size() == 0) {
                    summaries[count].setRelation(empty);
                } else {
                    summaries[count].setRelation(curRelation.get(count));
                }
                if (longRelation.size() == 0) {
                    summaries[count].setLongTermRelation(empty);
                } else {
                    summaries[count].setLongTermRelation(longRelation.get(count));
                }
                summaries[count].setChange(change.get(count));
                summaries[count].setLongChange(longChange.get(count));
            }else {
                summaries[count].setLongTermRelation(empty);
                summaries[count].setRelation(empty);
                summaries[count].setChange(empty);
                summaries[count].setLongChange(empty);
            }
            count++;
        }
        return summaries;
    }

    List<double[]> getRelationFromDir(File dir){
        if (!dir.exists() || dir.listFiles().length == 0) {
            return new ArrayList<>();
        }
        File file = dir.listFiles()[0];
        return getRelationFromFile(file);
    }

    List<double[]> getRelationFromFile(File file){
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try{
            String string = new String(ByteTools.readBytesFromFile(file), "UTF-8");
            List<String[]> results = new ArrayList<>();
            String[] rooms = string.split("\n");
            for(String room : rooms){
                results.add(room.split(","));
            }
            return SharedLogic.convertLearningData(results);
        }catch (Exception e){
            debuggingService.nonFatalDebug("Failed to read learning file", e);
            return new ArrayList<>();
        }
    }

    @Scheduled(cron = "0 0 */3 * * *")
    public void calculatePearson() throws IOException {
        List<double[]> results = new ArrayList<>();
        long time = System.currentTimeMillis();
        File dir = new File("PiCenter/Learning/Daily/Pearson");
        File file = new File(dir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.createNewFile();
            results = pearsonCorrelation.generateCorrelation(CalenderConverter.getMonthDayYear(System.currentTimeMillis() - DAY, "/", ":"), CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "/", ":"));
        } catch (IOException ioe) {
            debuggingService.nonFatalDebug("Failed to create learning file", ioe);
            return;
        } catch (ExecutionException ee) {
            debuggingService.nonFatalDebug("Failed to execute learning thread", ee);
            return;
        } catch (InterruptedException ie) {
            debuggingService.nonFatalDebug("Learning thread interrupted", ie);
            return;
        }catch (Exception e) {
            debuggingService.nonFatalDebug("Failed to execute pearson", e);
            return;
        }
        learningDataToFile(results, file);
    }

    @Scheduled(cron = "0 0 */12 * * *")
    public void calculatePearsons() throws IOException, Exception {
        List<double[]> results = new ArrayList<>();
        long time = System.currentTimeMillis();
        File dir = new File("PiCenter/Learning/Daily/Pearson");
        File wdir = new File("PiCenter/Learning/Weekly/Pearson");
        File ldir = new File("PiCenter/Learning/LongTerm/Pearson");
        File file = new File(ldir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time);
        File wfile = new File(wdir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!ldir.exists()) {
            ldir.mkdirs();
        }
        if (!wdir.exists()) {
            wdir.mkdirs();
        }
        results = pearsonCorrelation.generateCorrelation(CalenderConverter.getMonthDayYear(System.currentTimeMillis() - DAY, "/", ":"), CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "/", ":"));
        learningDataToFile(results, new File(dir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time));
        results = pearsonCorrelation.generateCorrelation(CalenderConverter.getMonthDayYear(System.currentTimeMillis() - WEEK, "/", ":"), CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "/", ":"));
        learningDataToFile(results, wfile);
        results = pearsonCorrelation.generateCorrelation("11/01/2019", CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "/", ":"));
        learningDataToFile(results, file);
        boolean first = true;
        for(File stat : wdir.listFiles()){
            if(first){
                results = getRelationFromFile(stat);
                first = false;
            }else{
                results = SharedLogic.compareLearningData(results, getRelationFromFile(stat));
            }
        }
        for(File stat : ldir.listFiles()){
            if(first){
                results = getRelationFromFile(stat);
                first = false;
            }else{
                results = SharedLogic.compareLearningData(results, getRelationFromFile(stat));
            }
        }
        for(File stat : dir.listFiles()){
            if(first){
                results = getRelationFromFile(stat);
                first = false;
            }else{
                results = SharedLogic.compareLearningData(results, getRelationFromFile(stat));
            }
        }
        learningDataToFile(results, file);
    }

    @Scheduled(cron = "0 0 */3 * * *")
    public void calculateRelationship() throws IOException {
        List<double[]> results = new ArrayList<>();
        long time = System.currentTimeMillis();
        File dir = new File("PiCenter/Learning/Daily/Change");
        File file = new File(dir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.createNewFile();
            results = calculateCorrelation(2);
        } catch (IOException ioe) {
            debuggingService.nonFatalDebug("Failed to create learning file", ioe);
            return;
        } catch (ExecutionException ee) {
            debuggingService.nonFatalDebug("Failed to execute learning thread", ee);
            return;
        } catch (InterruptedException ie) {
            debuggingService.nonFatalDebug("Learning thread interrupted", ie);
            return;
        }
        learningDataToFile(results, file);
    }

    private boolean learningDataToFile(List<double[]> results, File file) {
        String out = "";
        boolean first = true;
        for (double[] result : results) {
            if (first) {
                out = result[0] + "," + result[1];
                first = false;
            } else {
                out += "\n" + result[0] + "," + result[1];
            }
        }
        try {
            ByteTools.writeBytesToFile(file, out.getBytes("UTF-8"));
            return true;
        } catch (Exception ioe) {
            debuggingService.nonFatalDebug("Failed to write to learning file", ioe);
            return false;
        }
    }

    @Scheduled(cron = "0 0 */7 * * *")
    public void calculateRelationships() throws IOException {
        List<double[]> results = new ArrayList<>();
        long time = System.currentTimeMillis();
        File dir = new File("PiCenter/Learning/Daily/Change");
        File wdir = new File("PiCenter/Learning/Weekly/Change");
        File ldir = new File("PiCenter/Learning/LongTerm/Change");
        File file = new File(ldir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time);
        File wfile = new File(wdir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time);
        if (!dir.exists()) {
            dir.mkdirs();
            return;
        }
        if (!ldir.exists()) {
            ldir.mkdirs();
        }
        if (!wdir.exists()) {
            wdir.mkdirs();
        }
        calculateRelationship(1, new File(dir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time));
        calculateRelationship(7, wfile);
        boolean first = true;
        for(File stat : wdir.listFiles()){
            if(first){
                results = getRelationFromFile(stat);
                first = false;
            }else{
                results = SharedLogic.compareLearningData(results, getRelationFromFile(stat));
            }
        }
        for(File stat : ldir.listFiles()){
            if(first){
                results = getRelationFromFile(stat);
                first = false;
            }else{
                results = SharedLogic.compareLearningData(results, getRelationFromFile(stat));
            }
        }
        for(File stat : dir.listFiles()){
            if(first){
                results = getRelationFromFile(stat);
                first = false;
            }else{
                results = SharedLogic.compareLearningData(results, getRelationFromFile(stat));
            }
        }
        learningDataToFile(results, file);
    }

    private boolean calculateRelationship(int days) throws IOException {
        List<double[]> results = new ArrayList<>();
        long time = System.currentTimeMillis();
        File dir = new File("PiCenter/Learning/Daily/Change");
        File file = new File(dir, "/" + CalenderConverter.getMonthDayYear(time, "-", "-") + time);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return calculateRelationship(days, file);
    }

    private boolean calculateRelationship(int days, File file) throws IOException {
        List<double[]> results = new ArrayList<>();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                debuggingService.nonFatalDebug("Failed to create learning file", ioe);
                return false;
            }
        }
        try {
            results = calculateCorrelation(days);
        } catch (ExecutionException ee) {
            debuggingService.nonFatalDebug("Failed to execute learning thread", ee);
            return false;
        } catch (InterruptedException ie) {
            debuggingService.nonFatalDebug("Learning thread interrupted", ie);
            return false;
        }
        return learningDataToFile(results, file);
    }

    List<double[]> calculateCorrelation(int days) throws ExecutionException, InterruptedException {
        List<double[]> results = new ArrayList<>();
        List<long[]> intervals = getLearningIntervals(days);
        List<double[]> outsideSlopes = processLearning(intervals, "outside");
        Future<double[]>[] futures = new Future[4];
        futures[0] = threadPoolTaskExecutor.submit(() -> {
            return scoreDiffs(getRelation(outsideSlopes, processLearning(intervals, "office")));
        });
        futures[1] = threadPoolTaskExecutor.submit(() -> {
            return scoreDiffs(getRelation(outsideSlopes, processLearning(intervals, "livingroom")));
        });
        futures[2] = threadPoolTaskExecutor.submit(() -> {
            return scoreDiffs(getRelation(outsideSlopes, processLearning(intervals, "bedroom")));
        });
        futures[3] = threadPoolTaskExecutor.submit(() -> {
            return scoreDiffs(getRelation(outsideSlopes, processLearning(intervals, "serverroom")));
        });
        for (Future<double[]> future : futures) {
            results.add(future.get());
        }
        return SharedLogic.roundLearningData(results, 3);
    }

    List<double[]> processLearning(List<long[]> intervals, String room) {
        return getLearningSlopes(getLearningMeans(intervals, room));
    }

    List<long[]> getLearningIntervals(int days) {
        long now = System.currentTimeMillis();
        String today = CalenderConverter.getMonthDayYear(now, "/", ":");
        String yesterday = CalenderConverter.getMonthDayYear((now - (days * DAY)), "/", ":");
        return SharedLogic.generateIntervalWindows(SharedLogic.getTimeIntervals(yesterday, today, 1), 1);
    }

    List<double[]> getLearningMeans(List<long[]> windows, String room) {
        return getAverages(windows, room, 2);
    }

    List<double[]> getLearningSlopes(List<double[]> means) {
        List<double[]> slopes = new ArrayList<>();
        for (int i = 1; i < means.size(); i++) {
            double[] tY = {means.get(i)[0], means.get(i - 1)[0]};
            double[] hY = {means.get(i)[1], means.get(i - 1)[1]};
            double[] slope = new double[2];
            if (tY[0] == 0 || tY[1] == 0) {
                slope[0] = 0;
            } else {
                slope[0] = tY[0] - tY[1];
            }
            if (hY[0] == 0 || hY[1] == 0) {
                slope[1] = 0;
            } else {
                slope[1] = hY[0] - hY[1];
            }
            slopes.add(slope);
        }
        return slopes;
    }

    static List<double[]> getRelation(List<double[]> outsideSlopes, List<double[]> compareSlopes) {
        List<double[]> diffs = new ArrayList();
        for (int i = 0; i < outsideSlopes.size(); i++) {
            double[] diff = {0, 0};
            double[] t = {outsideSlopes.get(i)[0], compareSlopes.get(i)[0]};
            double[] h = {outsideSlopes.get(i)[1], compareSlopes.get(i)[1]};
            if (t[0] == 0 || t[1] == 0) {
                diff[0] = -1;
            }
            if (h[0] == 0 || h[1] == 0) {
                diff[1] = -1;
            }
            if ((t[0] > 0 && t[1] < 0) || (t[0] < 0 && t[1] > 0)) {
                diff[0] = 10;
            }
            if ((h[0] > 0 && h[1] < 0) || (h[0] < 0 && h[1] > 0)) {
                diff[1] = 10;
            }
            if (diff[0] != -1 && diff[0] != 10) {
                diff[0] = t[0] - t[1];
                diff[0] = abs(diff[0]);
                if (diff[0] > 10) {
                    diff[0] = 10;
                }
            }
            if (diff[1] != -1 && diff[1] != 10) {
                diff[1] = h[0] - h[1];
                diff[1] = abs(diff[1]);
                if (diff[1] > 10) {
                    diff[1] = 10;
                }
            }
            diffs.add(diff);
        }
        return diffs;
    }

    double[] scoreDiffs(List<double[]> diffs) {
        int tcount = 0, hcount = 0;
        double tsum = 0, hsum = 0;
        for (double[] diff : diffs) {
            if (diff[0] != -1) {
                tcount++;
                tsum += diff[0];
            }
            if (diff[1] != -1) {
                hcount++;
                hsum += diff[1];
            }
        }
        double[] scores = {((tsum / tcount) / 10), ((hsum / hcount) / 10)};
        return scores;
    }

    List<double[]> getAverages(List<long[]> intervals, String room, int precision) {
        List<double[]> results = new ArrayList<>();
        for (long[] interval : intervals) {
            results.add(getAverage(interval, room, precision));
        }
        return results;
    }

    ScatterPoint[] generateRoomScatterPoints(String start, String end, String room, String type) {
        List<Reading> insideReadings = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(getMillisFromDateString(start, "/"), getMillisFromDateString(end, "/") + DAY, room);
        ScatterPoint[] points = new ScatterPoint[insideReadings.size()];
        int count = 0;
        for (Reading reading : insideReadings) {
            ScatterPoint point = new ScatterPoint();
            point.setTime(reading.getTime());
            if (type.contains("temp")) {
                point.setInside(reading.getTemp());
                point.setOutside(Integer.parseInt((int) getAverage(reading.getTime(), 6 * (MINUTE), "outside", 0)[0] + ""));
            } else {
                point.setInside(reading.getHumidity());
                point.setOutside(Integer.parseInt((int) getAverage(reading.getTime(), 6 * (MINUTE), "outside", 0)[1] + ""));
            }
            points[count] = point;
            count++;
        }
        return points;
    }

    /**
     * Returns array of temp and humidity average of readings between time +- window in room room.
     *
     * @param time
     * @param window
     * @param room
     * @return
     */
    double[] getAverage(long time, long window, String room, int precision) {
        long startTime = time - window;
        long endTime = time + window;
        List<Reading> readings = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(startTime, endTime, room);
        return getMeans(getSums(readings), precision);
    }

    /**
     * Returns array of temp and humidity average of readings between time +- window in room room.
     *
     * @param time
     * @param window
     * @param room
     * @return
     */
    double[] getAverage(long time, long window, String room) {
        long startTime = time - window;
        long endTime = time + window;
        List<Reading> readings = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(startTime, endTime, room);
        return getMeans(getSums(readings));
    }

    /**
     * Returns array of temp and humidity average of readings between window[0] and window[1]..
     *
     * @param window: long[2]
     * @param room
     * @return
     */
    double[] getAverage(long window[], String room) {
        if (window.length != 2) {
            return null;
        }
        long startTime = window[0];
        long endTime = window[1];
        List<Reading> readings = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(startTime, endTime, room);
        return getMeans(getSums(readings));
    }

    double[] getAverage(long window[], String room, int precision) {
        if (window.length != 2) {
            return null;
        }
        long startTime = window[0];
        long endTime = window[1];
        List<Reading> readings = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(startTime, endTime, room);
        return getMeans(getSums(readings), precision);
    }


    String[] getHighLow(String date, String room) {
        long start = CalenderConverter.getMillisFromDateString(date, "/");
        long end = start + CalenderConverter.DAY;
        List<Reading> readings = readingRepository.findByTimeBetweenAndRoomOrderByTempDesc(start, end, room);
        String[] results = new String[2];
        if (readings.size() == 0) {
            results[0] = "0-0";
            results[1] = "0-0";
            return results;
        }
        results[0] = readings.get(0).getTemp() + "-" + readings.get(readings.size() - 1).getTemp();
        Collections.sort(readings, (r1, r2) -> {
            return r2.getHumidity() - r1.getHumidity();
        });
        results[1] = readings.get(0).getHumidity() + "-" + readings.get(readings.size() - 1).getHumidity();
        return results;
    }

    String[] getHighLow(long start, long end, String room) {
        List<Reading> readings = readingRepository.findByTimeBetweenAndRoomOrderByTempDesc(start, end, room);
        String[] results = new String[2];
        if (readings.size() == 0) {
            results[0] = "0-0";
            results[1] = "0-0";
            return results;
        }
        results[0] = readings.get(0).getTemp() + "-" + readings.get(readings.size() - 1).getTemp();
        Collections.sort(readings, (r1, r2) -> {
            return r2.getHumidity() - r1.getHumidity();
        });
        results[1] = readings.get(0).getHumidity() + "-" + readings.get(readings.size() - 1).getHumidity();
        return results;
    }

    private double[] getMeans(SumBean sums) {
        double[] means = new double[2];
        means[0] = Double.parseDouble(SharedLogic.round(findMean(sums.getSums()[0], sums.getCount()), 1));
        means[1] = Double.parseDouble(SharedLogic.round(findMean(sums.getSums()[1], sums.getCount()), 1));
        return means;
    }

    private double[] getMeans(SumBean sums, int precision) {
        double[] means = new double[2];
        means[0] = Double.parseDouble(SharedLogic.round(findMean(sums.getSums()[0], sums.getCount()), precision));
        means[1] = Double.parseDouble(SharedLogic.round(findMean(sums.getSums()[1], sums.getCount()), precision));
        return means;
    }

    private SumBean getSums(List<Reading> readings) {
        int count = 0;
        BigDecimal temp = new BigDecimal("0");
        BigDecimal humidity = new BigDecimal("0");
        for (Reading reading : readings) {
            temp = temp.add(new BigDecimal(reading.getTemp()));
            humidity = humidity.add(new BigDecimal(reading.getHumidity()));
            count++;
        }
        BigDecimal[] sums = {temp, humidity};
        return new SumBean(sums, count);
    }

    private RoomSummary getRoomSummary(String roomName, int precision) {
        Long time = System.currentTimeMillis();
        String[] temp = new String[7];
        String[] humidity = new String[7];
        String[] tempDevi = new String[7];
        String[] humidityDevi = new String[7];
        String[] stats = new String[2];
        List<Reading> reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.HOUR, time, roomName);
        if (reads.isEmpty()) {
            for (int i = 0; i < 2; i++) {
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        } else {
            List<Reading> readNow = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - (5 * MINUTE), time, roomName);
            if (readNow.size() == 0) {
                if (precision > 0) {
                    temp[0] = "0." + SharedLogic.getZeros(precision);
                    humidity[0] = "0." + SharedLogic.getZeros(precision);
                    tempDevi[0] = "0" + "." + SharedLogic.getZeros(precision);
                    humidityDevi[0] = "0" + "." + SharedLogic.getZeros(precision);
                } else {
                    temp[0] = "0";
                    humidity[0] = "0";
                    tempDevi[0] = "0";
                    humidityDevi[0] = "0";
                }
            } else {
                if (precision > 0) {
                    temp[0] = reads.get(0).getTemp() + "." + SharedLogic.getZeros(precision);
                    humidity[0] = reads.get(0).getHumidity() + "." + SharedLogic.getZeros(precision);
                    tempDevi[0] = "0" + "." + SharedLogic.getZeros(precision);
                    humidityDevi[0] = "0" + "." + SharedLogic.getZeros(precision);
                } else {
                    temp[0] = reads.get(0).getTemp() + "";
                    humidity[0] = reads.get(0).getHumidity() + "";
                    tempDevi[0] = "0";
                    humidityDevi[0] = "0";
                }
            }
            stats = getTempStats(reads, precision);
            temp[1] = stats[0];
            tempDevi[1] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[1] = stats[0];
            humidityDevi[1] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.TWO_HOUR, time, roomName);
        if (reads.isEmpty()) {
            for (int i = 2; i < 3; i++) {
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        } else {
            stats = getTempStats(reads, precision);
            temp[2] = stats[0];
            tempDevi[2] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[2] = stats[0];
            humidityDevi[2] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.THREE_HOUR, time, roomName);
        if (reads.isEmpty()) {
            for (int i = 3; i < 4; i++) {
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        } else {
            stats = getTempStats(reads, precision);
            temp[3] = stats[0];
            tempDevi[3] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[3] = stats[0];
            humidityDevi[3] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.SIX_HOUR, time, roomName);
        if (reads.isEmpty()) {
            for (int i = 4; i < 5; i++) {
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        } else {
            stats = getTempStats(reads, precision);
            temp[4] = stats[0];
            tempDevi[4] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[4] = stats[0];
            humidityDevi[4] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - StatConstants.TWELVE_HOUR, time, roomName);
        if (reads.isEmpty()) {
            for (int i = 5; i < 6; i++) {
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        } else {
            stats = getTempStats(reads, precision);
            temp[5] = stats[0];
            tempDevi[5] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[5] = stats[0];
            humidityDevi[5] = stats[1];
        }
        reads = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(time - (2 * StatConstants.TWELVE_HOUR), time, roomName);
        if (reads.isEmpty()) {
            for (int i = 5; i < 6; i++) {
                temp[i] = "0";
                humidity[i] = "0";
                tempDevi[i] = "0";
                humidityDevi[i] = "0";
            }
        } else {
            stats = getTempStats(reads, precision);
            temp[6] = stats[0];
            tempDevi[6] = stats[1];
            stats = getHumidityStats(reads, precision);
            humidity[6] = stats[0];
            humidityDevi[6] = stats[1];
        }
        return new RoomSummary(roomName, temp, humidity, tempDevi, humidityDevi);
    }

    private String[] getTempStats(List<Reading> readings, int precision) {
        BigDecimal sum = new BigDecimal(0);
        int count = 0;
        for (Reading reading : readings) {
            BigDecimal temp = new BigDecimal(reading.getTemp());
            sum = sum.add(temp);
            count++;
        }
        return findStats(RoomConstants.READING_TYPE_TEMP, sum, precision, readings, true);
    }

    private String[] getHumidityStats(List<Reading> readings, int precision) {
        BigDecimal sum = new BigDecimal(0);
        int count = 0;
        for (Reading reading : readings) {
            BigDecimal temp = new BigDecimal(reading.getHumidity());
            sum = sum.add(temp);
            count++;
        }
        return findStats(RoomConstants.READING_TYPE_HUMIDITY, sum, precision, readings, true);
    }

    private String[] findStats(int readingType, BigDecimal sum, int precision, List<Reading> readings, boolean sample) {
        //0 = mean, 1 = standard deviation
        String[] results = new String[2];
        BigDecimal mean = new BigDecimal(findMean(sum, readings.size()));
        results[0] = SharedLogic.round(mean.toString(), precision);
        BigDecimal deviation = findStandardDeviation(readingType, mean, sample, readings, precision);
        results[1] = SharedLogic.round(deviation.toString(), precision);
        return results;
    }

    private BigDecimal findStandardDeviation(int readingType, BigDecimal mean, boolean sample, List<Reading> readings, int precision) {
        List<BigDecimal> squares = new ArrayList<>();
        for (Reading reading : readings) {
            BigDecimal read;
            if (readingType == RoomConstants.READING_TYPE_TEMP) {
                read = new BigDecimal(reading.getTemp());
            } else {
                read = new BigDecimal(reading.getHumidity());
            }
            read = new BigDecimal(read.doubleValue() - mean.doubleValue());
            squares.add(new BigDecimal(read.doubleValue() * read.doubleValue()));
        }
        BigDecimal sum = new BigDecimal("0");
        for (BigDecimal square : squares) {
            sum = sum.add(square);
        }
        BigDecimal square;
        if (sample) {
            square = new BigDecimal(findMean(sum, squares.size() - 1));
        } else {
            square = new BigDecimal(findMean(sum, squares.size()));
        }
        square = BigDecimalMath.sqrt(square, 100);
        return square;
    }

    BigDecimal findStandardDeviation(BigDecimal mean, boolean sample, ScatterInterval interval, boolean outside) {
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        List<BigDecimal> squares = new ArrayList<>();
        for(ScatterPoint point : interval.getInterval()){
            BigDecimal read;
            if (outside) {
                read = new BigDecimal(point.getOutside());
            } else {
                read = new BigDecimal(point.getInside());
            }
            read = new BigDecimal(read.doubleValue() - mean.doubleValue());
            squares.add(new BigDecimal(read.doubleValue() * read.doubleValue()));
        }
        BigDecimal sum = new BigDecimal("0");
        for (BigDecimal square : squares) {
            sum = sum.add(square, mc);
        }
        BigDecimal square;
        if (sample) {
            square = new BigDecimal(findMean(sum, squares.size() - 1));
        } else {
            square = new BigDecimal(findMean(sum, squares.size()));
        }
        square = BigDecimalMath.sqrt(square, 100);
        return square;
    }

    String findMean(BigDecimal sum, int count) {
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        if (count == 0) {
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

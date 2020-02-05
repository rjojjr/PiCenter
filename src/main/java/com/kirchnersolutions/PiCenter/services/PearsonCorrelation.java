package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.ScatterInterval;
import com.kirchnersolutions.PiCenter.servers.beans.ScatterPoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.From;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@DependsOn({"statService", "chartService"})
@Component
public class PearsonCorrelation {

    private ChartService chartService;
    private StatService statService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public PearsonCorrelation(ChartService chartService, StatService statService, ThreadPoolTaskExecutor threadPoolTaskExecutor){
        this.chartService = chartService;
        this.statService = statService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    List<double[]> generateCorrelation(String from, String to) throws Exception{
        ScatterInterval[] tempScat = chartService.generateScatterData(new ChartRequest(from, to, "temp"));
        ScatterInterval[] humScat= chartService.generateScatterData(new ChartRequest(from, to, "hum"));
        Future<double[]>[] futures = new Future[4];
        double[] empty = {0.0, 0.0};
        int count = 0;
        if(count == 0 && tempScat[count].getInterval().length == 0 || humScat[count].getInterval().length == 0){
            futures[0] = threadPoolTaskExecutor.submit(() -> {
                return empty;
            });

            futures[1] = threadPoolTaskExecutor.submit(() -> {
                return empty;
            });
            futures[2] = threadPoolTaskExecutor.submit(() -> {
                return empty;
            });
            futures[3] = threadPoolTaskExecutor.submit(() -> {
                return empty;
            });
        }else if(count == 0){
            final BigDecimal outsideTempMean;
            final BigDecimal outsideHumDev;
            final BigDecimal outsideHumMean;
            final BigDecimal outsideTempDev;
            outsideTempMean = getMean(tempScat[0], true);
            outsideHumMean = getMean(humScat[0], true);
            outsideTempDev = statService.findStandardDeviation(outsideTempMean, true, tempScat[0], true);
            outsideHumDev = statService.findStandardDeviation(outsideHumMean, true, humScat[0], true);
            futures[0] = threadPoolTaskExecutor.submit(() -> {
                return getPearsons(tempScat[0], humScat[0], outsideTempMean, outsideTempDev, outsideHumMean, outsideHumDev);
            });

            for(ScatterInterval scatterInterval : tempScat){
                if(count == 0){

                }else if (scatterInterval.getInterval().length == 0 || humScat[count].getInterval().length == 0) {
                    futures[count] = threadPoolTaskExecutor.submit(() -> {
                        return empty;
                    });
                }else {
                    int c = count;
                    futures[count] = threadPoolTaskExecutor.submit(() -> {
                        return getPearsons(tempScat[c], humScat[c], outsideTempMean, outsideTempDev, outsideHumMean, outsideHumDev);
                    });
                }
                count++;
            }
        }


        List<double[]> results = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            results.add(futures[i].get());
        }
        return results;
    }

    double[] getPearsons(ScatterInterval tempInterval, ScatterInterval humInterval, BigDecimal outsideTempMean, BigDecimal outsideTempDeviation, BigDecimal outsideHumMean, BigDecimal outsideHumDeviation){
        double[] result = {getPearson(tempInterval, outsideTempMean, outsideTempDeviation), getPearson(humInterval, outsideHumMean, outsideHumDeviation)};
        return result;
    }

    double getPearson(ScatterInterval interval, BigDecimal outsideMean, BigDecimal outsideDeviation){
        BigDecimal insideMean = getMean(interval, false);
        BigDecimal deviation = statService.findStandardDeviation(insideMean, true, interval, false);
        return getPearson(getCovariance(interval, insideMean, outsideMean), deviation, outsideDeviation).doubleValue();
    }

    BigDecimal getMean(ScatterInterval interval, boolean outside){
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        BigDecimal sum = new BigDecimal(0);
        int count = 0;
        for(ScatterPoint point : interval.getInterval()){
            if(outside){
                sum = sum.add(new BigDecimal(point.getOutside()), mc);
            }else{
                sum = sum.add(new BigDecimal(point.getInside()), mc);
            }
            count++;
        }
        return new BigDecimal(statService.findMean(sum, count));
    }

    BigDecimal getCovariance(ScatterInterval interval, BigDecimal insideMean, BigDecimal outsideMean){
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        BigDecimal sum = new BigDecimal(0);
        int count = 0;
        for(ScatterPoint point : interval.getInterval()){
            BigDecimal inDiff = insideMean.subtract(new BigDecimal(point.getInside()), mc);
            BigDecimal outDiff = outsideMean.subtract(new BigDecimal(point.getOutside()), mc);
            sum = sum.add(inDiff.multiply(outDiff), mc);
            count++;
        }
        return sum.divide(new BigDecimal(count -1), mc);
    }

    BigDecimal getPearson(BigDecimal covariance, BigDecimal inDeviation, BigDecimal outDeviation){
        MathContext mc = new MathContext(10, RoundingMode.HALF_UP);
        BigDecimal product = inDeviation.multiply(outDeviation, mc);
        return covariance.divide(product, mc);
    }

}

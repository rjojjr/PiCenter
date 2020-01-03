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
        BigDecimal outsideTempMean = getMean(tempScat[0], true);
        BigDecimal outsideHumMean = getMean(humScat[0], true);
        BigDecimal outsideTempDev = statService.findStandardDeviation(outsideTempMean, true, tempScat[0], true);
        BigDecimal outsideHumDev = statService.findStandardDeviation(outsideHumMean, true, humScat[0], true);
        Future<double[]>[] futures = new Future[4];
        futures[0] = threadPoolTaskExecutor.submit(() -> {
            return getPearsons(tempScat[0], humScat[0], outsideTempMean, outsideTempDev, outsideHumMean, outsideHumDev);
        });
        futures[1] = threadPoolTaskExecutor.submit(() -> {
            return getPearsons(tempScat[1], humScat[1], outsideTempMean, outsideTempDev, outsideHumMean, outsideHumDev);
        });
        futures[2] = threadPoolTaskExecutor.submit(() -> {
            return getPearsons(tempScat[2], humScat[2], outsideTempMean, outsideTempDev, outsideHumMean, outsideHumDev);
        });
        futures[3] = threadPoolTaskExecutor.submit(() -> {
            return getPearsons(tempScat[3], humScat[3], outsideTempMean, outsideTempDev, outsideHumMean, outsideHumDev);
        });
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

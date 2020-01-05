package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.servers.beans.ScatterPoint;
import com.kirchnersolutions.utilities.ByteTools;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import sun.security.krb5.internal.tools.Klist;

import java.awt.color.ICC_Profile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class PolynomialService {

    private StatService statService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private DebuggingService debuggingService;

    @Autowired
    public PolynomialService(StatService statService, ThreadPoolTaskExecutor threadPoolTaskExecutor, DebuggingService debuggingService){
        this.statService = statService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.debuggingService = debuggingService;
    }

    @Scheduled(cron = "0 0 */4 * * *")
    public void calcPolys(){
        File tDir = new File("PiCenter/Learning/Daily/Poly/temp");
        File hDir = new File("PiCenter/Learning/Daily/Poly/hum");
        long time = System.currentTimeMillis();
        String start = CalenderConverter.getMonthDayYear(time - CalenderConverter.DAY, "/", "/");
        String end = CalenderConverter.getMonthDayYear(time, "/", "/");
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "office", "temp", tDir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "livingroom", "temp", tDir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "bedroom", "temp", tDir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "serverroom", "temp", tDir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "office", "hum", hDir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "livingroom", "hum", hDir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "bedroom", "hum", hDir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "serverroom", "hum", hDir);
        });
    }

    private void generatePolys(String start, String end, String room, String type, File dir){
        polyToFile(dir, fitPolys(start, end, room, type), room);
    }

    List<double[]> fitPolys(String start, String end, String room, String type){
        List<double[]> curves = new ArrayList<>();
        ScatterPoint[] points = statService.generateRoomScatterPoints(start, end, room, type);
        final WeightedObservedPoints obs = new WeightedObservedPoints();
        for(ScatterPoint point : points){
            obs.add(point.getOutside(), point.getInside());
        }
        Future<double[]>[] futures = new Future[4];
        futures[0] = threadPoolTaskExecutor.submit(() -> {
            return fitPoly(obs, 4);
        });
        futures[1] = threadPoolTaskExecutor.submit(() -> {
            return fitPoly(obs, 6);
        });
        futures[2] = threadPoolTaskExecutor.submit(() -> {
            return fitPoly(obs, 8);
        });
        futures[3] = threadPoolTaskExecutor.submit(() -> {
            return fitPoly(obs, 10);
        });
        double[] failed = {0};
        int count = 4;
        for(Future<double[]> future : futures){
            try{
                curves.add(future.get());
            }catch (InterruptedException ie){
                curves.add(failed);
                debuggingService.nonFatalDebug("Failed to calc room " + room + " degree " + count + " " + type + " polynomial", ie);
                ie.printStackTrace();
            }catch (ExecutionException ee){
                curves.add(failed);
                debuggingService.nonFatalDebug("Failed to calc room " + room + " degree " + count + " " + type + " polynomial", ee);
                ee.printStackTrace();
            }
            count+= 2;
        }
        return curves;
    }

    public String[][] getLatestCurveHTML(String type){
        File dir = new File("PiCenter/Learning/Daily/Poly/temp");
        if(type.equals("temp")){
            dir = new File("PiCenter/Learning/Daily/Poly/temp");
        }else{
            dir = new File("PiCenter/Learning/Daily/Poly/hum");
        }
        String[][] htmls = new String[4][4];
        List<double[]>[] doubleCurves = getLatestCurves(dir);
        int count = 0;
        for(List<double[]> curves : doubleCurves){
            String[] html = new String[4];
            for(int i = 0; i < 4; i++){
                double[] coef = curves.get(i);
                if(coef.length == 1){
                    html[i] = "<p>Error calculating curve</p>";
                }else{
                    switch (i){
                        case 0:
                            if(coef.length != 5){
                                html[i] = "<p>Error calculating curve</p>";
                            }else{
                                html[i] = "<p> f(x) = " + coef[0] + "x<sup>4</sup> + " + coef[1] + "x<sup>3</sup> + " + coef[2] + "x<sup>2</sup> + " + coef[3] + "x + " + coef[4] + "</p>";
                            }
                            break;
                        case 1:
                            if(coef.length != 7){
                                html[i] = "<p>Error calculating curve</p>";
                            }else{
                                html[i] = "<p> f(x) = " + coef[0] + "x<sup>6</sup> + " + coef[1] + "x<sup>5</sup> + " + coef[2] + "x<sup>4</sup> + " + coef[3] + "x<sup>3</sup> + " + coef[4]  + "x<sup>2</sup> + " + coef[5] + "x + " + coef[6] + "</p>";
                            }
                            break;
                        case 2:
                            if(coef.length != 9){
                                html[i] = "<p>Error calculating curve</p>";
                            }else{
                                html[i] = "<p> f(x) = " + coef[0] + "x<sup>8</sup> + " + coef[1] + "x<sup>7</sup> + " + coef[2] + "x<sup>6</sup> + " + coef[3] + "x<sup>5</sup> + " + coef[4]  + "x<sup>4</sup> + " + coef[5] + "x<sup>3</sup> + " + coef[6] + "x<sup>2</sup> + " + coef[7] + "x + " + coef[8] + "</p>";
                            }
                            break;
                        case 3:
                            if(coef.length != 11){
                                html[i] = "<p>Error calculating curve</p>";
                            }else{
                                html[i] = "<p> f(x) = " + coef[0] + "x<sup>10</sup> + " + coef[1] + "x<sup>9</sup> + " + coef[2] + "x<sup>8</sup> + " + coef[3] + "x<sup>7</sup> + " + coef[4]  + "x<sup>6</sup> + " + coef[5] + "x<sup>5</sup> + " + coef[6] + "x<sup>4</sup> + " + coef[7] + "x<sup>3</sup> + " + coef[8] + "x<sup>2</sup> + " + coef[9] + "x + " + coef[10] + "</p>";
                            }
                            break;
                    }
                }
            }
            htmls[count] = html;
            count++;
        }
        return htmls;
    }

    List<double[]>[] getLatestCurves(File dir){
        List<double[]>[] rooms = new List[4];
        int count = 0;
        for (String room : RoomConstants.insideRooms){
            rooms[count] = getLatestCurves(dir, room);
        }
        return rooms;
    }

    List<double[]> getLatestCurves(File dir, String room){
        if(!dir.exists()){
            dir.mkdirs();
            return new ArrayList<>();
        }
        File[] files = dir.listFiles();
        File target = null;
        for(File file : files){
            if(file.getName().contains(room)){
                target = file;
                break;
            }
        }
        if(target == null){
            return new ArrayList<>();
        }
        try{
            String csv = new String(ByteTools.readBytesFromFile(target), "UTF-8");
            return fromCSV(csv);
        }catch (Exception e){
            debuggingService.nonFatalDebug("Failed to read " + room + " polynomial file", e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    void polyToFile(File dir, List<double[]> curves, String room){
        if(!dir.exists()){
            dir.mkdirs();
        }
        File polys = new File(dir, "/" + room + "-" + CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "-", "-") + System.currentTimeMillis() + ".csv");
        try{
            polys.createNewFile();
            ByteTools.writeBytesToFile(polys, toCSV(curves).getBytes("UTF-8"));
        }catch (IOException e){
            debuggingService.nonFatalDebug("Failed to create " + room + " polynomial file", e);
            e.printStackTrace();
        }catch(Exception e){
            debuggingService.nonFatalDebug("Failed to write to " + room + " polynomial file", e);
            e.printStackTrace();
        }
    }

    private List<double[]> fromCSV(String CSV){
        List<double[]> values = new ArrayList<>();
        String[] lines = CSV.split("\n");
        for(String line : lines){
            String[] strings = line.split(",");
            double[] val = new double[strings.length];
            int count = 0;
            for(String value : strings){
                val[count] = Double.parseDouble(value);
                count++;
            }
            values.add(val);
        }
        return values;
    }

    private String toCSV(List<double[]> list){
        String CSV = "";
        boolean first = true, firstLine = true;
        for(double[] values : list){
            String CSVLine = "";
            for(double value : values){
                if(first){
                    CSVLine = value + "";
                    first = false;
                }else {
                    CSVLine+= "," + value;
                }
            }
            if(firstLine){
                CSV = CSVLine;
                firstLine = false;
            }else {
                CSV+= "\n" + CSVLine;
            }
        }
        return CSV;
    }

    private double[] fitPoly(WeightedObservedPoints obs, int degree){
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        final double[] coeff = fitter.fit(obs.toList());
        return coeff;
    }

    private double[] fitSixDegree(WeightedObservedPoints obs){
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(6);
        final double[] coeff = fitter.fit(obs.toList());
        return coeff;
    }

    private double[] fitEightDegree(WeightedObservedPoints obs){
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(8);
        final double[] coeff = fitter.fit(obs.toList());
        return coeff;
    }

}

package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.servers.beans.ScatterPoint;
import com.kirchnersolutions.utilities.ByteTools;
import com.kirchnersolutions.utilities.CalenderConverter;

import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class PolynomialService {

    private StatService statService;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private DebuggingService debuggingService;

    private static final Comparator<File> lastModified = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            return o1.lastModified() == o2.lastModified() ? 0 : (o1.lastModified() < o2.lastModified() ? 1 : -1);
        }
    };

    @Autowired
    public PolynomialService(StatService statService, ThreadPoolTaskExecutor threadPoolTaskExecutor, DebuggingService debuggingService) {
        this.statService = statService;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.debuggingService = debuggingService;
    }

    @Scheduled(cron = "0 0 */6 * * *")
    public void calcPolys() {
        File dir = new File("PiCenter/Learning/Daily/Poly");
        long time = System.currentTimeMillis();
        String start = CalenderConverter.getMonthDayYear(time - (4 * CalenderConverter.WEEK), "/", "/");
        String end = CalenderConverter.getMonthDayYear(time, "/", "/");
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "office", "temp", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "livingroom", "temp", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "bedroom", "temp", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "serverroom", "temp", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "office", "hum", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "livingroom", "hum", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "bedroom", "hum", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "serverroom", "hum", dir);
        });
        long t = System.currentTimeMillis();
        ;
        threadPoolTaskExecutor.execute(() -> {
            polyToFile(new File(dir, "/temp"), fitOutsidePolys(time - (4 * CalenderConverter.WEEK), t, "temp"), "outside");
        });
        threadPoolTaskExecutor.execute(() -> {
            polyToFile(new File(dir, "/hum"), fitOutsidePolys(time - (4 * CalenderConverter.WEEK), t, "hum"), "outside");
        });
    }

    private void generatePolys(String start, String end, String room, String type, File dir) {
        polyToFile(new File(dir, "/" + type), fitPolys(start, end, room, type, dir), room);
    }

    List<double[]> fitPolys(String start, String end, String room, String type, File dir) {
        File ndir = new File(dir, "/" + type);
        List<double[]> prevCurves = getInitialGuesses(ndir, room);
        List<double[]> curves = new ArrayList<>();
        ScatterPoint[] points = statService.generateRoomScatterPoints(start, end, room, type);
        final WeightedObservedPoints obs = new WeightedObservedPoints();
        for (ScatterPoint point : points) {
            obs.add(point.getOutside(), point.getInside());
        }
        Future<double[]>[] futures = new Future[5];
        futures[0] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 1, 100, prevCurves.get(0));
        });
        futures[1] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 2, 100, prevCurves.get(1));
        });
        futures[2] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 3, 100, prevCurves.get(2));
        });
        futures[3] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 4, 100, prevCurves.get(3));
        });
        futures[4] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 5, 100, prevCurves.get(4));
        });
        double[] failed = {0};
        int count = 2;
        for (Future<double[]> future : futures) {
            try {
                curves.add(future.get());
            } catch (InterruptedException ie) {
                curves.add(failed);
                debuggingService.nonFatalDebug("Failed to calc room " + room + " degree " + count + " " + type + " polynomial", ie);
                ie.printStackTrace();
            } catch (ExecutionException ee) {
                curves.add(failed);
                debuggingService.nonFatalDebug("Failed to calc room " + room + " degree " + count + " " + type + " polynomial", ee);
                ee.printStackTrace();
            }
            count += 2;
        }
        return curves;
    }

    private List<double[]> getInitialGuesses(File dir, String room) {
        List<double[]> prevCurves = getLatestCurves(dir, room);
        if(prevCurves.size() < 5){
            return PolynomialHelper.dummyCoef();
        }
        return prevCurves;
    }

    List<double[]> fitOutsidePolys(long start, long end, String type) {
        File dir = new File("PiCenter/Learning/Daily/Poly/temp");
        if (type.equals("hum")) {
            dir = new File("PiCenter/Learning/Daily/Poly/hum");
        }
        List<double[]> prevCurves = getInitialGuesses(dir, "outside");
        List<double[]> curves = new ArrayList<>();
        List<Reading> readings = statService.getReadings(start, end, "outside");
        final WeightedObservedPoints obs = new WeightedObservedPoints();
        for (Reading point : readings) {
            if (type.equals("temp")) {
                obs.add(point.getTime(), point.getTemp());
            } else {
                obs.add(point.getTime(), point.getHumidity());
            }
        }
        Future<double[]>[] futures = new Future[5];
        futures[0] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 1, 1, prevCurves.get(0));
        });
        futures[1] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 2, 1, prevCurves.get(1));
        });
        futures[2] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 3, 1, prevCurves.get(2));
        });
        futures[3] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 4, 1, prevCurves.get(3));
        });
        futures[4] = threadPoolTaskExecutor.submit(() -> {
            return PolynomialHelper.fitPoly(obs, 5, 1, prevCurves.get(4));
        });
        double[] failed = {0};
        int count = 2;
        for (Future<double[]> future : futures) {
            try {
                curves.add(future.get());
            } catch (InterruptedException ie) {
                curves.add(failed);
                debuggingService.nonFatalDebug("Failed to calc outside " + " degree " + count + " " + type + " polynomial", ie);
                ie.printStackTrace();
            } catch (ExecutionException ee) {
                curves.add(failed);
                debuggingService.nonFatalDebug("Failed to calc outside " + " degree " + count + " " + type + " polynomial", ee);
                ee.printStackTrace();
            }
            count += 2;
        }
        return curves;
    }

    public String[][] getLatestCurveHTML(String type) {
        File dir = new File("PiCenter/Learning/Daily/Poly/hum");
        if (type.equals("temp")) {
            dir = new File("PiCenter/Learning/Daily/Poly/temp");
        }
        String[][] htmls = new String[5][5];
        List<double[]>[] doubleCurves = getLatestCurves(dir);
        int count = 0;
        for (List<double[]> curves : doubleCurves) {
            String[] html = new String[5];
            for (int i = 0; i < 4; i++) {
                double[] coef = curves.get(i);
                if (coef.length <= 1) {
                    html[i] = "<p>Error calculating curve</p>";
                } else {
                    switch (i) {
                        case 0:
                            if (coef.length != 2) {
                                html[i] = "<p>Error calculating curve</p>";
                            } else {
                                html[i] = "<p> f(x) = " + coef[1] + "x + " + coef[0] + "</p>";
                            }
                            break;
                        case 1:
                            if (coef.length != 3) {
                                html[i] = "<p>Error calculating curve</p>";
                            } else {
                                html[i] = "<p> f(x) = " + coef[2] + "x<sup>2</sup> + " + coef[1] + "x + " + coef[0] + "</p>";
                            }
                            break;
                        case 2:
                            if (coef.length != 4) {
                                html[i] = "<p>Error calculating curve</p>";
                            } else {
                                html[i] = "<p> f(x) = " + coef[3] + "x<sup>3</sup> + " + coef[2] + "x<sup>2</sup> + " + coef[1] + "x + " + coef[0] + "</p>";
                            }
                            break;
                        case 3:
                            if (coef.length != 5) {
                                html[i] = "<p>Error calculating curve</p>";
                            } else {
                                html[i] = "<p> f(x) = " + coef[4] + "x<sup>4</sup> + " + coef[3] + "x<sup>3</sup> + " + coef[2] + "x<sup>2</sup> + " + coef[1] + "x + " + coef[0] + "</p>";
                            }
                            break;
                        case 4:
                            if (coef.length != 6) {
                                html[i] = "<p>Error calculating curve</p>";
                            } else {
                                html[i] = "<p> f(x) = " + coef[5] + "x<sup>5</sup> + " + coef[4] + "x<sup>4</sup> + " + coef[3] + "x<sup>3</sup> + " + coef[2] + "x<sup>2</sup> + " + coef[1] + "x + " + coef[0] + "</p>";
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

    public void calcPolys(String start, String end) {
        File dir = new File("PiCenter/Learning/Daily/Poly");
        long time = System.currentTimeMillis();
        String ostart = CalenderConverter.getMonthDayYear(time - CalenderConverter.DAY, "/", "/");
        String oend = CalenderConverter.getMonthDayYear(time, "/", "/");
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "office", "temp", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "livingroom", "temp", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "bedroom", "temp", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "serverroom", "temp", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "office", "hum", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "livingroom", "hum", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "bedroom", "hum", dir);
        });
        threadPoolTaskExecutor.execute(() -> {
            generatePolys(start, end, "serverroom", "hum", dir);
        });
        long t = System.currentTimeMillis();
        ;
        threadPoolTaskExecutor.execute(() -> {
            polyToFile(new File(dir, "/temp"), fitOutsidePolys(CalenderConverter.getMillisFromDateString(start, "/"), CalenderConverter.getMillisFromDateString(end, "/") + CalenderConverter.DAY, "temp"), "outside");
        });
        threadPoolTaskExecutor.execute(() -> {
            polyToFile(new File(dir, "/hum"), fitOutsidePolys(CalenderConverter.getMillisFromDateString(start, "/"), CalenderConverter.getMillisFromDateString(end, "/") + CalenderConverter.DAY, "hum"), "outside");
        });
    }

    List<double[]>[] getLatestCurves(File dir) {
        List<double[]>[] rooms = new List[5];
        int count = 0;
        for (int i = 0; i < 5; i++) {
            rooms[count] = getLatestCurves(dir, RoomConstants.rooms[count]);
            count++;
        }
        return rooms;
    }

    List<double[]> getLatestCurves(File dir, String room) {
        if (!dir.exists()) {
            dir.mkdirs();
            return new ArrayList<>();
        }

        File[] files = dir.listFiles();
        Arrays.sort(files, lastModified);
        File target = null;
        for (File file : files) {
            if (file.getName().contains(room)) {
                target = file;
                break;
            }
        }
        if (target == null) {
            return new ArrayList<>();
        }
        try {
            String csv = new String(ByteTools.readBytesFromFile(target), "UTF-8");
            return fromCSV(csv);
        } catch (Exception e) {
            debuggingService.nonFatalDebug("Failed to read " + room + " polynomial file", e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    void polyToFile(File dir, List<double[]> curves, String room) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File polys = new File(dir, "/" + room + "-" + CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "-", "-") + System.currentTimeMillis() + ".csv");
        try {
            polys.createNewFile();
            ByteTools.writeBytesToFile(polys, toCSV(curves).getBytes("UTF-8"));
        } catch (IOException e) {
            debuggingService.nonFatalDebug("Failed to create " + room + " polynomial file", e);
            e.printStackTrace();
        } catch (Exception e) {
            debuggingService.nonFatalDebug("Failed to write to " + room + " polynomial file", e);
            e.printStackTrace();
        }
    }

    private List<double[]> fromCSV(String CSV) {
        List<double[]> values = new ArrayList<>();
        String[] lines = CSV.split("\n");
        for (String line : lines) {
            String[] strings = line.split(",");
            double[] val = new double[strings.length];
            int count = 0;
            for (String value : strings) {
                val[count] = Double.parseDouble(value);
                count++;
            }
            values.add(val);
        }
        return values;
    }

    private String toCSV(List<double[]> list) {
        String CSV = "";
        boolean firstLine = true;
        for (double[] values : list) {
            boolean first = true;
            String CSVLine = "";
            for (double val : values) {
                String value = String.format("%.3f", val);
                if (first) {
                    CSVLine = value;
                    first = false;
                } else {
                    CSVLine += "," + value;
                }
            }
            if (firstLine) {
                CSV = CSVLine;
                firstLine = false;
            } else {
                CSV += "\n" + CSVLine;
            }
        }
        return CSV;
    }

}

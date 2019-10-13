package com.kirchnersolutions.PiCenter.dev;

import com.kirchnersolutions.PiCenter.Configuration.SysVars;
import com.kirchnersolutions.utilities.ByteTools;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@DependsOn({"debuggingService"})
@Repository
public class DebugLogRepository {

    @Autowired
    private SysVars sysVars;
    @Autowired
    private DebuggingService debuggingService;

    private File exDir = new File("Database/Dev/Trace/DevelopmentExceptions");
    private File nonFatDir = new File("Database/Dev/Trace/NonFatalExceptions");
    private File ipDir = new File("Database/logs/IPDebug");
    private File stompDir = new File("Database/logs/StompDebug");
    private File socketDir = new File("Database/logs/SocketDebug");

    private volatile AtomicBoolean stompLock;

    public DebugLogRepository(){

    }

    @PostConstruct
    public void init() throws Exception{
        stompLock = debuggingService.getStompLock();
    }

    public List<LogBean> getBeans(){
        List<LogBean> list = new ArrayList<>();
        list.add(new LogBean("Trace", exDir.listFiles().length + ""));
        list.add(new LogBean("Exceptions", nonFatDir.listFiles().length + ""));
        list.add(new LogBean("IP", ipDir.listFiles().length + ""));
        list.add(new LogBean("Stomp", stompDir.listFiles().length + ""));
        list.add(new LogBean("Socket", socketDir.listFiles().length + ""));
        return list;
    }

    public String searchTraceLogs(Map<String, String> request, String name) {
        if(name.equals("Trace")){
            return SearchTraceLogs(request);
        }
        if(name.equals("Exceptions")){
            return SearchExceptionLogs(request);
        }
        if(name.equals("IP")){
            return SearchIPLogs(request);
        }
        if(name.equals("Stomp")){
            return SearchStompLogs(request);
        }
        if(name.equals("Socket")){
            return SearchSocketLogs(request);
        }
        return "No Result";
    }

    private String SearchExceptionLogs(Map<String, String> request) {
        String sdate = "", edate = "";
        if (request.containsKey("sdate")) {
            sdate = request.get("sdate");
        }
        if (request.containsKey("edate")) {
            edate = request.get("edate");
        }
        return searchException(sdate, edate, nonFatDir);
    }

    private String SearchTraceLogs(Map<String, String> request) {
        String sdate = "", edate = "";
        if (request.containsKey("sdate")) {
            sdate = request.get("sdate");
        }
        if (request.containsKey("edate")) {
            edate = request.get("edate");
        }
        return searchException(sdate, edate, exDir);
    }

    private String SearchStompLogs(Map<String, String> request) {
        while (stompLock.get()){

        }
        stompLock.set(true);
        String sdate = "", edate = "";
        if (request.containsKey("sdate")) {
            sdate = request.get("sdate");
        }
        if (request.containsKey("edate")) {
            edate = request.get("edate");
        }
        return searchException(sdate, edate, stompDir);
    }

    private String SearchSocketLogs(Map<String, String> request) {
        String sdate = "", edate = "";
        if (request.containsKey("sdate")) {
            sdate = request.get("sdate");
        }
        if (request.containsKey("edate")) {
            edate = request.get("edate");
        }
        return searchException(sdate, edate, socketDir);
    }

    private String SearchIPLogs(Map<String, String> request) {
        String sdate = "", edate = "";
        if (request.containsKey("sdate")) {
            sdate = request.get("sdate");
        }
        if (request.containsKey("edate")) {
            edate = request.get("edate");
        }
        return searchException(sdate, edate, ipDir);
    }

    private String searchException(String stime, String etime, File dir) {
        List<File> matches = new ArrayList<>();
        long sdate = 0;
        long edate = 0;
        try {
            if(!stime.equals("")){
                sdate = parseDate(stime);
            }
            if(!etime.equals("")){
                edate = parseDate(etime);
            }
            matches = compareDates(sdate, edate, dir);
            if(matches.isEmpty()){
                return "11%No Result";
            }
            synchronized (dir){
                Collections.sort(matches, new LogSorter());
                return parseExpLog(matches);
            }
        } catch (Exception e) {
            return "11%Improper argument formats";
        }
    }

    private String parseExpLog(List<File> matches) throws Exception {
        String buffer = new String("10%");
        boolean first = true;
        for (File file : matches) {
            byte[] bytes = ByteTools.readBytesFromFile(file);
            if(file.getParent().equals(exDir.getPath())){
                if (first) {
                    String tbuffer = new String(bytes, "UTF-8");
                    tbuffer = tbuffer.replaceAll(",", ":");
                    tbuffer = tbuffer.replaceAll("%", ":");
                    String[] temp = tbuffer.split(sysVars.getNewLineChar());
                    int count = 0;
                    for(String line : temp){
                        if(count == 0){
                            buffer+= temp[count];
                        }else if(count == 1){
                            buffer+= ";" + temp[1];
                        }else {
                            buffer+= " - " + temp[count];
                        }
                        count++;
                    }
                    first = false;
                } else {
                    String tbuffer = new String(bytes, "UTF-8");
                    tbuffer = tbuffer.replaceAll(",", ":");
                    tbuffer = tbuffer.replaceAll("%", ":");
                    String[] temp = tbuffer.split(sysVars.getNewLineChar());
                    int count = 0;
                    for(String line : temp){
                        if(count == 0){
                            buffer+= "," + temp[count];
                        }else if(count == 1){
                            buffer+= ";" + temp[1];
                        }else {
                            buffer+= " - " + temp[count];
                        }
                        count++;
                    }
                }
            }else{
                if (first) {
                    String tbuffer = new String(bytes, "UTF-8");
                    tbuffer = tbuffer.replaceAll(",", ":");
                    tbuffer = tbuffer.replaceAll("%", ":");
                    String[] temp = tbuffer.split(sysVars.getNewLineChar());
                    buffer+= temp[0];
                    buffer+= ";" + temp[1];
                    first = false;
                } else {
                    String tbuffer = new String(bytes, "UTF-8");
                    tbuffer = tbuffer.replaceAll(",", ":");
                    tbuffer = tbuffer.replaceAll("%", ":");
                    String[] temp = tbuffer.split(sysVars.getNewLineChar());
                    buffer+= "," + temp[0];
                    buffer+= ";" + temp[1];
                }
            }
        }
        return buffer.toString();
    }

    private List<File> compareDates(long stime, long etime, File dir) {
        List<File> matches = new ArrayList<>();
        for (File file : dir.listFiles()) {
            String[] temp = file.getName().split(".t");
            try {

                long ftime = Long.parseLong(temp[0]);
                if (stime != 0 && etime != 0) {
                    if (ftime >= stime && ftime <= etime) {
                        matches.add(file);
                    }
                } else if (stime != 0) {
                    if (ftime >= stime) {
                        matches.add(file);
                    }
                } else if (etime != 0) {
                    if (ftime <= etime) {
                        matches.add(file);
                    }
                } else {
                    matches = Arrays.asList(dir.listFiles());
                }
            } catch (Exception e) {
                System.err.println("Failed to parse exception log: " + file.getName());
            }
        }
        stompLock.set(false);
        return matches;
    }

    private long parseDate(String date) {
        String[] temp = date.split("~");
        String[] temp1 = temp[0].split("/");
        return CalenderConverter.getMillis(Integer.parseInt(temp1[0]), Integer.parseInt(temp1[1]),
                Integer.parseInt(temp1[2]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]));
    }

}

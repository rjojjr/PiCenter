package com.kirchnersolutions.PiCenter.dev;

import com.kirchnersolutions.PiCenter.Configuration.DevVars;
import com.kirchnersolutions.PiCenter.Configuration.SysVars;
import com.kirchnersolutions.utilities.ByteTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.kirchnersolutions.utilities.CalenderConverter;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

@DependsOn({"devVars"})
@Service
public class DebuggingService {

    @Autowired
    private DevVars devVars;
    @Autowired
    private SysVars sysVars;

    private File exDir = new File("Database/Dev/Trace/DevelopmentExceptions");
    private File nonFatDir = new File("Database/Dev/Trace/NonFatalExceptions");
    private File ipDir = new File("Database/logs/IPDebug");
    private File stompDir = new File("Database/logs/StompDebug");
    private File socketDir = new File("Database/logs/SocketDebug");
    private volatile AtomicBoolean stompLock = new AtomicBoolean(false);

    public DebuggingService(){
        if(!exDir.exists()){
            exDir.mkdirs();
        }
        if(!nonFatDir.exists()){
            nonFatDir.mkdirs();
        }
        if(!ipDir.exists()){
            ipDir.mkdirs();
        }
        if(!stompDir.exists()){
            stompDir.mkdirs();
        }
        if(!socketDir.exists()){
            socketDir.mkdirs();
        }
    }


    public AtomicBoolean getStompLock(){
        return stompLock;
    }

    public void throwDevException(DevelopmentException e) throws DevelopmentException {
        if(devVars.isDevExceptions()){
            File log = new File(exDir, "/" + System.currentTimeMillis() + ".txt");
            try{
                log.createNewFile();
                ByteTools.writeBytesToFile(log, (CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~")
                        + sysVars.getNewLineChar() + e.getMessage() + sysVars.getNewLineChar() + getStack(e)).getBytes("UTF-8"));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
            throw e;
        }
    }

    public void stompDebug(String message){
        if(devVars.isStompDebug()){
            while(stompLock.get()){

            }
            stompLock.set(true);
            synchronized (stompDir){

                File log = new File(stompDir, "/" + System.currentTimeMillis() + ".txt");
                try{
                    log.createNewFile();
                    ByteTools.writeBytesToFile(log, (CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~")
                            + sysVars.getNewLineChar() + message).getBytes("UTF-8"));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            stompLock.set(false);
            System.out.println(message + " " + CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~"));
        }
    }

    public void ipDebug(String log){
        if(devVars.isIpDebug()){
            synchronized (ipDir){
                File logf = new File(ipDir, "/" + System.currentTimeMillis() + ".txt");
                try{
                    logf.createNewFile();
                    ByteTools.writeBytesToFile(logf, (CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~")
                            + sysVars.getNewLineChar() + log).getBytes("UTF-8"));
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            System.out.println(log + " " + CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~"));
        }
    }

    public void nonFatalDebug(String log){
        if(devVars.isNonFatal()){
            File logf = new File(nonFatDir, "/" + System.currentTimeMillis() + ".txt");
            try{
                logf.createNewFile();
                ByteTools.writeBytesToFile(logf, (CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~")
                        + sysVars.getNewLineChar() + log).getBytes("UTF-8"));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            System.out.println(log + " " + CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~"));
        }
    }

    public void socketDebug(String log){
        if(devVars.isSocketDebug()){
            File logf = new File(socketDir, "/" + System.currentTimeMillis() + ".txt");
            try{
                logf.createNewFile();
                ByteTools.writeBytesToFile(logf, (CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~")
                        + sysVars.getNewLineChar() + log).getBytes("UTF-8"));
            }catch (Exception ex){
                ex.printStackTrace();
            }
            System.out.println(log + " " + CalenderConverter.getMonthDayYearHourMinuteSecond(System.currentTimeMillis(), "/", "~"));
        }
    }

    public boolean dumpLogs(String type){
        boolean sucess = false;
        switch (type){
            case "Trace":
                sucess = DumpLogs(exDir);
                break;
            case "Stomp":
                sucess = DumpLogs(stompDir);
                break;
            case "IP":
                sucess = DumpLogs(ipDir);
                break;
            case "Exceptions":
                sucess =  DumpLogs(nonFatDir);
                break;
            case "Socket":
                sucess =  DumpLogs(socketDir);
                break;
        }
        return sucess;
    }

    private boolean DumpLogs(File dir){
        for(File log: dir.listFiles()){
            log.delete();
        }
        return true;
    }

    public String getStack(Exception e){
        String out = "";
        boolean first = true;
        for(StackTraceElement el : e.getStackTrace()){
            if (first) {
                out = el.toString();
                first = false;
            }else {
                out+= sysVars.getNewLineChar() + el.toString();
            }
        }
        return out;
    }

}

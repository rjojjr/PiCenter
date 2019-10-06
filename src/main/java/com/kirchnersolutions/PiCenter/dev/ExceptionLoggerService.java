package com.kirchnersolutions.PiCenter.dev;

import com.kirchnersolutions.utilities.ByteTools;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ExceptionLoggerService {

    private final File dir = new File("Database");
    private final File devLogDir = new File(dir, "/Dev/Trace/DevelopmentExceptions"), unCaughtDir = new File(dir, "/Dev/Trace/UncaughtExceptions");

    public void writeDev(String msg){
        WriteDev(msg);
    }

    private void WriteDev(String msg){
        long timeStamp = System.currentTimeMillis();
        File exc = new File(devLogDir, "/" + CalenderConverter.getMonthDayYearHourMinuteSecond(timeStamp, ";", ":"));
        try{
            exc.createNewFile();
            msg+= "\r\n@ " + CalenderConverter.getMonthDayYearHourMinuteSecond(timeStamp, ";", ":");
            ByteTools.writeBytesToFile(exc, msg.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

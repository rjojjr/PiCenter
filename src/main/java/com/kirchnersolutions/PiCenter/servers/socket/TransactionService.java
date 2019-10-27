package com.kirchnersolutions.PiCenter.servers.socket;

import com.kirchnersolutions.PiCenter.Configuration.SysVars;
import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.dev.DevelopmentException;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.objects.ObjectFactory;
import com.kirchnersolutions.PiCenter.servers.objects.Results;
import com.kirchnersolutions.PiCenter.servers.objects.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    DebuggingService debuggingService;
    @Autowired
    private ReadingRepository readRepository;
    @Autowired
    private ObjectFactory objectFactory;
    @Autowired
    private SysVars sysVars;

    byte[] inputRequest(byte[] input) throws Exception{
        return parseInput(input);
    }

    private Results submitTransaction(Transaction transaction){
        //Implement room database check here.
        debuggingService.trace("Thread " + Thread.currentThread().getName() + " Submitting socket transaction");
        Results results = new Results();
        String room = transaction.getOperation();
        Map<String, String> put = transaction.getNewRows().get(0);
        int humidity = -1;
        int temp = -1;
        long time = -1;
        if (room != null && put != null && put.get("humidity") != null && put.get("temp") != null && put.get("time") != null){
            humidity = Integer.parseInt(put.get("humidity"));
            temp = Integer.parseInt(put.get("temp"));
            time = Long.parseLong(put.get("time"));
            Reading reading = new Reading(time, temp, humidity, room);
            if(readRepository.saveAndFlush(reading) != null){
                results.setSuccess(true);
            }else{
                results.setSuccess(false);
                debuggingService.nonFatalDebug("Failed to save reading from room " + room + " to database");
            }
        }else{
            results.setSuccess(false);
            debuggingService.nonFatalDebug("Failed to save reading from room " + room + " to database: Invalid arguments");
        }
        debuggingService.trace("Thread " + Thread.currentThread().getName() + " Parsed socket transaction");
        return results;
    }

    private byte[] parseInput(byte[] input) throws Exception{
        try{
            debuggingService.trace("Thread " + Thread.currentThread().getName() + " Parsing socket request");
            Results results = submitTransaction((Transaction)objectFactory.databaseObjectFactory(Base64.getDecoder().decode(input)));
            debuggingService.trace("Thread " + Thread.currentThread().getName() + " Returning socket response");
            return objectFactory.databaseSerialFactory(results);
        }catch (Exception e){
            debuggingService.trace("Thread " + Thread.currentThread().getName() + " Failed to parse socket request");
            //e.printStackTrace();
            String stackTrace = "";
            for(StackTraceElement trace : e.getStackTrace()){
                stackTrace+= sysVars.getNewLineChar() + trace.toString();
            }
            debuggingService.throwDevException(new DevelopmentException("Failed to parse socket request" + stackTrace));
            debuggingService.nonFatalDebug("Failed to parse socket request" + stackTrace);
            return null;
        }

    }

}

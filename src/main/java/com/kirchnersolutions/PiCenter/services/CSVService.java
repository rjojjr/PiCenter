package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.PiCenter.services.parsers.CSVParserImpl;
import com.kirchnersolutions.PiCenter.entites.DBItem;
import com.kirchnersolutions.utilities.ByteTools;
import com.kirchnersolutions.utilities.CalenderConverter;
import com.kirchnersolutions.utilities.CryptTools;
import com.kirchnersolutions.utilities.ZipTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class CSVService {

    ReadingRepository readingRepository;
    UserLogRepository userLogRepository;
    AppUserRepository appUserRepository;
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    DebuggingService debuggingService;

    private File dir, appUserDir, userLogDir, readingDir, downloadDir, downloadFileTempDir, downloadFile;

    @Autowired
    public CSVService(AppUserRepository appUserRepository, UserLogRepository userLogRepository, ReadingRepository readingRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor, DebuggingService debuggingService){
        this.appUserRepository = appUserRepository;
        this.readingRepository = readingRepository;
        this.userLogRepository = userLogRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.debuggingService = debuggingService;
        dir = new File("PiCenter/Backup");
        appUserDir = new File(dir, "/AppUsers");
        userLogDir = new File(dir, "/UserLogs");
        readingDir = new File(dir, "/Readings");
        downloadDir = new File(dir, "/Download");
        downloadFileTempDir = new File(downloadDir, "/PiCenterBackup");
        downloadFile = new File(downloadDir, "/PiCenterBackup.zip");
        if(!dir.exists()){
            dir.mkdirs();
            appUserDir.mkdirs();
            userLogDir.mkdirs();
            readingDir.mkdirs();
        }
    }

    private boolean backup(String table){
        try{
            if(makeCSVSwitch(table)){
                downloadFile.createNewFile();
                List<File> zipFiles = Arrays.asList(downloadFileTempDir.listFiles());
                ZipTools.zip(zipFiles, downloadFile.getPath());
            }
        }catch (Exception e){
            e.printStackTrace();
            debuggingService.nonFatalDebug("Failed to generate backup package", e);
        }

    }

    private boolean makeCSVSwitch(String table) throws IOException, Exception {
        if(!downloadFileTempDir.exists()){
            downloadFileTempDir.mkdirs();
        }
        File out;
        boolean success = false;
        switch (table.toLowerCase()){
            case "all":
                success = true;
                Future<Boolean>[] futures = new Future[3];
                futures[0] = threadPoolTaskExecutor.submit(new CSVThread("users"));
                futures[1] = threadPoolTaskExecutor.submit(new CSVThread("readings"));
                futures[2] = threadPoolTaskExecutor.submit(new CSVThread("userlogs"));
                boolean temp = futures[0].get();
                if(!temp){
                    success = false;
                }
                temp = futures[1].get();
                if(!temp){
                    success = false;
                }
                temp = futures[2].get();
                if(!temp){
                    success = false;
                }
                break;
            case "users":
                out = new File(downloadFileTempDir, "/Users_" + CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "-", "-") + ".csv");
                out.createNewFile();
                success = makeUserCSV(out);
                break;
            case "readings" :
                out = new File(downloadFileTempDir, "/Readings_" + CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "-", "-") + ".csv");
                out.createNewFile();
                success = makeReadingCSV(out);
                break;
            case "userlogs" :
                out = new File(downloadFileTempDir, "/UserLogs_" + CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "-", "-") + ".csv");
                out.createNewFile();
                success = makeUserLogCSV(out);
                break;
            default:
                break;
        }
        return success;
    }

    private class CSVThread implements Callable<Boolean>{

        private String table;

        CSVThread(String table){
            this.table = table;
        }

        public Boolean call() throws Exception{
            return makeCSVSwitch(table);
        }

    }

    private boolean makeUserCSV(File out){
        return makeCSV(out, new ArrayList<>(appUserRepository.getAll()));
    }

    private boolean makeReadingCSV(File out){
        return makeCSV(out, new ArrayList<>(readingRepository.getAll()));
    }

    private boolean makeUserLogCSV(File out){
        return makeCSV(out, new ArrayList<>(userLogRepository.getAll()));
    }

    private boolean makeCSV(File out, List<DBItem> items) {
        if(!out.exists()){
            return false;
        }
        String csv = parseItemsToCSV(items);
        try{
            File hash = new File(downloadFileTempDir, "/" + out.getName() + ".sha512");
            hash.createNewFile();
            ByteTools.writeBytesToFile(hash, hashCSV(csv));
        }catch (Exception e){
            e.printStackTrace();
            debuggingService.nonFatalDebug("Failed to generate CSV hash", e);
        }
        try{
            ByteTools.writeBytesToFile(out,
                    Base64.getEncoder().encode(
                            csv.getBytes("UTF-8")
                    ));
            return true;
        }catch (Exception e){
            //Log here
           e.printStackTrace();
           debuggingService.nonFatalDebug("Failed to generate CSV", e);
           return false;
        }
    }

    private String parseItemsToCSV(List<DBItem> dbItems){
        if(dbItems == null || dbItems.size() == 0){
            return null;
        }
        return new CSVParserImpl().parseToCSV(dbItems);
    }

    private List<DBItem> parseCSVToItemWithId(String CSV){
        return new CSVParserImpl().parseToList(CSV);
    }

    private List<DBItem> parseCSVToItemWithoutId(String CSV){
        return new CSVParserImpl().parseToListWithoutId(CSV);
    }

    private byte[] hashCSV(String csv) throws UnsupportedEncodingException, Exception {
        byte[] hash = new byte[1];
        for(String line : csv.split("\r\n")){
            hash = CryptTools.getSHA512(csv.getBytes("UTF-8"), hash);
        }
        return Base64.getEncoder().encode(hash);
    }

    private boolean compareHash(String hash1, String hash2){
        return hash1.equals(hash2);
    }

}

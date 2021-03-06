package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.PiCenter.services.parsers.CSVParserImpl;
import com.kirchnersolutions.PiCenter.entites.DBItem;
import com.kirchnersolutions.utilities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
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

@DependsOn({"threadPoolTaskExecutor"})
@Service
public class CSVService {

    ReadingRepository readingRepository;
    UserLogRepository userLogRepository;
    AppUserRepository appUserRepository;
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    DebuggingService debuggingService;

    private File dir, appUserDir, userLogDir, readingDir, downloadDir, downloadFileTempDir, downloadFile, restoreDir, auto, manual;

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
        auto = new File(dir, "/automated");
        manual = new File(dir, "/manual");
        downloadFileTempDir = new File(downloadDir, "/PiCenterBackup");
        downloadFile = new File(downloadDir, "/PiCenterDownload.zip");
        restoreDir = new File(dir, "/Restore");
        if(!dir.exists()){
            dir.mkdirs();
            restoreDir.mkdirs();
            auto.mkdirs();
            manual.mkdirs();
            downloadDir.mkdirs();
        }
    }

    public boolean generateDownload(String table, boolean hash){
        try{
            return GenerateDownload(table, hash);
        }catch (Exception e){
            debuggingService.nonFatalDebug("Failed to delete backup temp files", e);
            return false;
        }
    }

    public boolean generateManualBackup(){
        return manual();
    }

    public boolean restoreCSV(){
        try{
            return restore();
        }catch (Exception e){
            debuggingService.nonFatalDebug("Failed to restore CSV", e);
            return false;
        }
    }

    private boolean restore() throws Exception{
        for(File csv : restoreDir.listFiles()){
            List<DBItem> items = new ArrayList<>();
            String CSV = new String(ByteTools.readBytesFromFile(csv), "UTF-8");
            items = (new CSVParserImpl()).parseToListWithoutId(CSV);
            switch(items.get(0).getType()){
                case "AppUser":
                    for(DBItem item : items){
                        appUserRepository.saveAndFlush((AppUser)item);
                    }
                    break;
                case "Reading":
                    for(DBItem item : items){
                        readingRepository.saveAndFlush((Reading) item);
                    }
                    break;
                case "UserLog":
                    for(DBItem item : items){
                        userLogRepository.saveAndFlush((UserLog) item);
                    }
                    break;
            }
        }
        return DeleteTools.delete(restoreDir.listFiles());
    }

    private boolean manual(){
        try{
            if(makeCSVSwitch("all", true)){
                File manBk = new File(manual, "/PiCenterManualBackup_" + CalenderConverter.getMonthDayYearHourMinute(System.currentTimeMillis(), "-", ":") + ".zip");
                manBk.createNewFile();
                List<File> zipFiles = Arrays.asList(downloadFileTempDir.listFiles());
                if(ZipTools.zip(zipFiles, manBk.getPath())){
                    List<File> files = new ArrayList<>();
                    files.add(downloadFileTempDir);
                    DeleteTools.delete(files);
                    return true;
                }
                List<File> files = new ArrayList<>();
                files.add(downloadFileTempDir);
                files.add(manBk);
                DeleteTools.delete(files);
                debuggingService.nonFatalDebug("Failed to generate backup package");
                return false;
            }
            List<File> files = new ArrayList<>();
            files.add(downloadFileTempDir);
            DeleteTools.delete(files);
            return false;
        }catch (Exception e){
            debuggingService.nonFatalDebug("Failed to generate manual download", e);
            return false;
        }
    }

    private boolean GenerateDownload(String table, boolean hash) throws Exception{
        try{
            if(makeCSVSwitch(table, hash)){
                downloadFile.createNewFile();
                List<File> zipFiles = Arrays.asList(downloadFileTempDir.listFiles());
                if(ZipTools.zip(zipFiles, downloadFile.getPath())){
                    List<File> files = new ArrayList<>();
                    files.add(downloadFileTempDir);
                    DeleteTools.delete(files);
                    return true;
                }
                List<File> files = new ArrayList<>();
                files.add(downloadFileTempDir);
                files.add(downloadFile);
                DeleteTools.delete(files);
                debuggingService.nonFatalDebug("Failed to generate backup package");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            debuggingService.nonFatalDebug("Failed to generate backup package", e);
            List<File> files = new ArrayList<>();
            files.add(downloadFileTempDir);
            files.add(downloadFile);
            DeleteTools.delete(files);
            return false;
        }
        List<File> files = new ArrayList<>();
        files.add(downloadFileTempDir);
        files.add(downloadFile);
        DeleteTools.delete(files);
        debuggingService.nonFatalDebug("Failed to generate backup package");
        return false;
    }

    private boolean makeCSVSwitch(String table, boolean hash) throws IOException, Exception {
        if(!downloadFileTempDir.exists()){
            downloadFileTempDir.mkdirs();
        }

        boolean success = false;
        switch (table.toLowerCase()){
            case "all":
                success = true;
                Future<Boolean>[] futures = new Future[3];
                futures[0] = threadPoolTaskExecutor.submit(new CSVThread("users", hash));
                futures[1] = threadPoolTaskExecutor.submit(new CSVThread("readings", hash));
                futures[2] = threadPoolTaskExecutor.submit(new CSVThread("userlogs", hash));
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
                File userOut = new File(downloadFileTempDir, "/Users_" + CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "-", "-") + ".csv");
                userOut.createNewFile();
                success = makeUserCSV(userOut, hash);
                break;
            case "readings" :
                File readingOut = new File(downloadFileTempDir, "/Readings_" + CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "-", "-") + ".csv");
                readingOut.createNewFile();
                success = makeReadingCSV(readingOut, hash);
                break;
            case "userlogs" :
                File logOut = new File(downloadFileTempDir, "/UserLogs_" + CalenderConverter.getMonthDayYear(System.currentTimeMillis(), "-", "-") + ".csv");
                logOut.createNewFile();
                success = makeUserLogCSV(logOut, hash);
                break;
            default:
                break;
        }
        return success;
    }

    private class CSVThread implements Callable<Boolean>{

        private String table;
        private boolean hash;

        CSVThread(String table, boolean hash){
            this.table = table;
            this.hash = hash;
        }

        public Boolean call() throws Exception{
            return makeCSVSwitch(table, hash);
        }

    }

    private boolean makeUserCSV(File out, boolean hash){
        return makeCSV(out, new ArrayList<>(appUserRepository.getAll()), hash);
    }

    private boolean makeReadingCSV(File out, boolean hash){
        return makeCSV(out, new ArrayList<>(readingRepository.getAll()), hash);
    }

    private boolean makeUserLogCSV(File out, boolean hash){
        return makeCSV(out, new ArrayList<>(userLogRepository.getAll()), hash);
    }

    private boolean makeCSV(File out, List<DBItem> items, boolean hash) {
        if(!out.exists()){
            return false;
        }
        String csv = parseItemsToCSV(items);
        if(hash){
            try{
                File hashFile = new File(downloadFileTempDir, "/" + out.getName() + ".sha512");
                hashFile.createNewFile();
                ByteTools.writeBytesToFile(hashFile, hashCSV(csv));
            }catch (Exception e){
                e.printStackTrace();
                debuggingService.nonFatalDebug("Failed to generate CSV hash", e);
            }
        }

        try{
            ByteTools.writeBytesToFile(out,
                            csv.getBytes("UTF-8")
                    );
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

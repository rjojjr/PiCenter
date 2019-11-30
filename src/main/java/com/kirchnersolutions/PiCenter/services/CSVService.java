package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.PiCenter.services.parsers.CSVParserImpl;
import com.kirchnersolutions.PiCenter.entites.DBItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CSVService {

    ReadingRepository readingRepository;
    UserLogRepository userLogRepository;
    AppUserRepository appUserRepository;
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private File dir, appUserDir, userLogDir, readingDir, downloadDir;

    @Autowired
    public CSVService(AppUserRepository appUserRepository, UserLogRepository userLogRepository, ReadingRepository readingRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor){
        this.appUserRepository = appUserRepository;
        this.readingRepository = readingRepository;
        this.userLogRepository = userLogRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        dir = new File("PiCenter/Backup");
        appUserDir = new File(dir, "/AppUsers");
        userLogDir = new File(dir, "/UserLogs");
        readingDir = new File(dir, "/Readings");
        downloadDir = new File(dir, "/Download");
        if(!dir.exists()){
            dir.mkdirs();
            appUserDir.mkdirs();
            userLogDir.mkdirs();
            readingDir.mkdirs();
        }
    }

    private String parseItemToCSV(List<DBItem> dbItems){
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

}

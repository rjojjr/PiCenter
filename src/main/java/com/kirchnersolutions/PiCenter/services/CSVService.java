package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.PiCenter.services.interfaces.AppUserCSVImpl;
import com.kirchnersolutions.PiCenter.services.interfaces.DBItem;
import com.kirchnersolutions.PiCenter.services.interfaces.ReadingCSVImpl;
import com.kirchnersolutions.PiCenter.services.interfaces.UserLogCSVImpl;
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

    private File dir, appUserDir, userLogDir, readingDir;

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
        if(!dir.exists()){
            dir.mkdirs();
            appUserDir.mkdirs();
            userLogDir.mkdirs();
            readingDir.mkdirs();
        }
    }

    private String parseAppUserToCSV(List<DBItem> users){
        if(users == null || users.size() == 0){
            return null;
        }
        try{
            AppUser typeTest = (AppUser)users.get(0);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid item type: AppUsers required");
        }
        return new AppUserCSVImpl().parseToCSV(users);
    }

    private List<DBItem> parseCSVToAppUserWithId(String CSV){
        return new AppUserCSVImpl().parseToList(CSV);
    }

    private List<DBItem> parseCSVToAppUserWithoutId(String CSV){
        return new AppUserCSVImpl().parseToListWithoutId(CSV);
    }

    private String parseReadingToCSV(List<DBItem> readings){
        if(readings == null || readings.size() == 0){
            return null;
        }
        try{
            Reading typeTest = (Reading)readings.get(0);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid item type: Readings required");
        }
        return new ReadingCSVImpl().parseToCSV(readings);
    }

    private List<DBItem> parseCSVToReadingWithId(String CSV){
        return new ReadingCSVImpl().parseToList(CSV);
    }

    private List<DBItem> parseCSVToReadingWithoutId(String CSV){
        return new ReadingCSVImpl().parseToListWithoutId(CSV);
    }

    private String parseUserLogToCSV(List<DBItem> userLogs){
        if(userLogs == null || userLogs.size() == 0){
            return null;
        }
        try{
            UserLog typeTest = (UserLog)userLogs.get(0);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid item type: UserLogs required");
        }
        return new UserLogCSVImpl().parseToCSV(userLogs);
    }

    private List<DBItem> parseCSVToUserLogWithId(String CSV){
        return new UserLogCSVImpl().parseToList(CSV);
    }

    private List<DBItem> parseCSVToUserLogWithoutId(String CSV){
        return new UserLogCSVImpl().parseToListWithoutId(CSV);
    }

}

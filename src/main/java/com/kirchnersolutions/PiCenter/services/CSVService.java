package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.PiCenter.services.interfaces.AppUserCSVImpl;
import com.kirchnersolutions.PiCenter.services.interfaces.DBItem;
import com.kirchnersolutions.PiCenter.services.interfaces.ReadingCSVImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CSVService {

    ReadingRepository readingRepository;
    UserLogRepository userLogRepository;
    AppUserRepository appUserRepository;

    private File dir, appUserDir, userLogDir, readingDir;

    @Autowired
    public CSVService(AppUserRepository appUserRepository, UserLogRepository userLogRepository, ReadingRepository readingRepository){
        this.appUserRepository = appUserRepository;
        this.readingRepository = readingRepository;
        this.userLogRepository = userLogRepository;
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
            throw new IllegalArgumentException("Invalid item type: AppUser required");
        }
        return new AppUserCSVImpl().parseToCSV(users);
    }

    private String parseReadingToCSV(List<DBItem> readings){
        if(readings == null || readings.size() == 0){
            return null;
        }
        try{
            Reading typeTest = (Reading)readings.get(0);
        }catch (Exception e){
            throw new IllegalArgumentException("Invalid item type: AppUser required");
        }
        return new ReadingCSVImpl().parseToCSV(readings);
    }

}

package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.entites.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {MainConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CSVServiceIntegrationTest {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    ReadingRepository readingRepository;
    @Autowired
    UserLogRepository userLogRepository;
    @Autowired
    CSVService csvService;

    File dir = new File("PiCenter/Backup");
    File downloadDir = new File(dir, "/Download");

    @Before
    public void setUp(){
        List<AppUser> users = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            users.add(new AppUser(System.currentTimeMillis(), "user" + i, "test", "user", "password", false));
        }
        appUserRepository.saveAll(users);
        List<Reading> readings = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            readings.add(new Reading(System.currentTimeMillis(), 71, 33, "room"));
        }
        readingRepository.saveAll(readings);
        List<UserLog> logs = new ArrayList<>();
        for(int i = 0; i < 100; i++){
           logs.add(new UserLog(Long.parseLong("1"), "action", System.currentTimeMillis()));
        }
        userLogRepository.saveAll(logs);
    }

    @After
    public void cleanUp(){
        appUserRepository.deleteAll();
        userLogRepository.deleteAll();
        readingRepository.deleteAll();
    }

    @Test
    public void whenMakeAllCSV_oneFile(){
        csvService.generateDownload("readings", false);
        assertEquals(1, downloadDir.listFiles().length);
    }

}

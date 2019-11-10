package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.constants.StatConstants;
import com.kirchnersolutions.PiCenter.entites.AppUser;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.RoomSummary;
import com.kirchnersolutions.utilities.CryptTools;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DependsOn({"userService"})
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
//@ContextConfiguration(classes = {MainConfig.class})
@ContextConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SummaryServiceIntegrationTest {

    /*@Autowired
    ApplicationContext context;*/
    @Autowired
    private ReadingRepository readingRepository;
    @Autowired
    private SummaryService summaryService;

    private boolean populateTestDB(){
        Long time = System.currentTimeMillis() - (StatConstants.TWELVE_HOUR * 2);
        Long interval = (StatConstants.TWELVE_HOUR * 2) / 1000;
        for(String room : RoomConstants.rooms){
            for(int i = 0; i < 1000; i++){
                Reading reading = new Reading((i * interval) + time, 77, 51, room);
                readingRepository.saveAndFlush(reading);
            }
        }
        return true;
    }

    /*@BeforeClass
    public static void setUp() throws Exception {
        populateTestDB();
        Long time = System.currentTimeMillis() - (StatConstants.TWELVE_HOUR * 2);
        Long interval = (StatConstants.TWELVE_HOUR * 2) / 1000;
        for(String room : RoomConstants.rooms){
            for(int i = 0; i < 1000; i++){
                Reading reading = new Reading((i * interval) + time, 77, 51, room);
                readingRepository.saveAndFlush(reading);
            }
        }
    }*/

    @Test
    public void whenPrecision1_Return77Temp51Humidity(){
        populateTestDB();
        RoomSummary[] summaries = summaryService.getRoomSummaries(1);
        for (int i = 0; i < RoomConstants.rooms.length; i++){
            for(String temp : summaries[i].getTemps()){
                assertEquals("temp != 77.0", "77.0", temp);
            }
            for(String humidity : summaries[i].getHumiditys()){
                assertEquals("humididty != 51.0", "50.0", humidity);
            }
        }
    }

}

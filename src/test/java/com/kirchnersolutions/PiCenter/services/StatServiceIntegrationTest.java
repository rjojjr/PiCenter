package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.constants.StatConstants;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.RoomSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@DependsOn({"summaryService"})
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {MainConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StatServiceIntegrationTest {

    @Autowired
    private ReadingRepository readingRepository;
    @Autowired
    private StatService statService;

    private boolean populateTestDB(){
        Long time = System.currentTimeMillis() - (StatConstants.TWELVE_HOUR * 2);
        Long interval = (StatConstants.TWELVE_HOUR * 2) / 100;
        List<Reading> readings = new ArrayList<>();
        for(String room : RoomConstants.rooms){
            for(int i = 0; i < 100; i++){
                Reading reading = new Reading((i * interval) + time, 77, 51, room);
                readings.add(reading);
            }
        }
        readingRepository.saveAll(readings);
        return true;
    }

    /*@BeforeClass
    public static void setUp() throws Exception {
        populateTestDB();
    }*/

    @Test
    public void whenPrecision_ReturnZeros(){
        populateTestDB();
        RoomSummary[] summaries = statService.getRoomSummaries(2);
        for (int i = 0; i < RoomConstants.rooms.length; i++){
            for(String temp : summaries[i].getTemps()){
                assertEquals("temp != 77.00", "77.00", temp);
            }
            for(String humidity : summaries[i].getHumiditys()){
                assertEquals("humidity != 51.00", "51.00", humidity);
            }
            for(String temp : summaries[i].getTempDevi()){
                assertEquals("temp deviation wrong precision", 2, temp.split("\\.")[1].toCharArray().length);
            }
            for(String humidity : summaries[i].getHumidityDevi()){
                assertEquals("humidity deviation wrong precision", 2, humidity.split("\\.")[1].toCharArray().length);
            }
        }
        readingRepository.truncateReadings();
    }

}

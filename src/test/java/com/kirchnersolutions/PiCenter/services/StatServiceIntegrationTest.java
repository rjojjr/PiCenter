package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.constants.StatConstants;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.RoomSummary;
import com.kirchnersolutions.utilities.CalenderConverter;
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

    private String populateHighLowTestDB(){
        Long time = System.currentTimeMillis();
        List<Reading> readings = new ArrayList<>();
        for(String room : RoomConstants.rooms){
            for(int i = 0; i < 50; i++){
                Reading reading = new Reading(System.currentTimeMillis(), 77, 51, room);
                readings.add(reading);
            }
            for(int i = 0; i < 50; i++){
                Reading reading = new Reading(System.currentTimeMillis(), 70, 40, room);
                readings.add(reading);
            }
        }
        readingRepository.saveAll(readings);
        return CalenderConverter.getMonthDayYear(time, "/", "");
    }

    private boolean populateHighLowTestDB(String start, String end){
        Long startTime = CalenderConverter.getMillisFromDateString(start, "/");
        Long endTime = CalenderConverter.getMillisFromDateString(end, "/");
        Long interval = (endTime - startTime) / 100;
        List<Reading> readings = new ArrayList<>();
        for(String room : RoomConstants.rooms){
            for(int i = 0; i < 100; i++){
                if(i % 2 == 0){
                    Reading reading = new Reading((i * interval) + startTime, 70, 40, room);
                    readings.add(reading);
                }else{
                    Reading reading = new Reading((i * interval) + startTime, 77, 51, room);
                    readings.add(reading);
                }
            }
        }
        readingRepository.saveAll(readings);
        return true;
    }

    @Test
    public void whenPrecision_ReturnZeros() throws Exception{
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

    @Test
    public void whenGetHighLow_returnHighLow() {
        String date = populateHighLowTestDB();
        for(String room : RoomConstants.rooms){
            String[] hl = statService.getHighLow(date, room);
            assertEquals(2, hl.length);
            assertEquals("77-70", hl[0]);
            assertEquals("51-40", hl[1]);
        }
        readingRepository.truncateReadings();
    }



    @Test
    public void whenNoResultGetHighLow_returnZeros() {
        for(String room : RoomConstants.rooms){
            String[] hl = statService.getHighLow("12/15/2019", room);
            assertEquals(2, hl.length);
            assertEquals("0-0", hl[0]);
            assertEquals("0-0", hl[1]);
        }
    }

}

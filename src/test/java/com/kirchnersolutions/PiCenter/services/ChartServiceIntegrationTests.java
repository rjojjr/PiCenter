package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.utilities.CalenderConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {MainConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChartServiceIntegrationTests {

    @Autowired
    private ReadingRepository readingRepository;

    @Autowired
    private ChartService chartService;

    private boolean populateHighLowTestDB(String start, String end){
        Long startTime = CalenderConverter.getMillisFromDateString(start, "/");
        Long endTime = CalenderConverter.getMillisFromDateString(end, "/") + CalenderConverter.DAY;
        Long interval = (endTime - startTime) / 200;
        List<Reading> readings = new ArrayList<>();
        for(String room : RoomConstants.rooms){
            for(int i = 0; i < 200; i++){
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
    public void whenGetHighLowRange_returnHighLow() {
        populateHighLowTestDB("12/01/2019", "12/07/2019");
        for(String room : RoomConstants.rooms){
            List<String[]> hl = chartService.getHighLow("12/01/2019", "12/07/2019", room);
            assertEquals(7, hl.size());
            for(String[] strings : hl){
                assertEquals(room + " Temp failed", "77-70", strings[0]);
                assertEquals(room + " Humidity failed", "51-40", strings[1]);
            }

        }
        readingRepository.truncateReadings();
    }

}

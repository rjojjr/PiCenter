package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.constants.RoomConstants;
import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import com.kirchnersolutions.PiCenter.servers.beans.ChartRequest;
import com.kirchnersolutions.PiCenter.servers.beans.DiffInterval;
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
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {MainConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChartServiceIntegrationTests {

    @Autowired
    private ReadingRepository readingRepository;

    @Autowired
    private ChartService chartService;

    private boolean populateHighLowTestDB(String start, String end) {
        CountDownLatch latch = new CountDownLatch(1);
        Long startTime = CalenderConverter.getMillisFromDateString(start, "/");
        Long endTime = CalenderConverter.getMillisFromDateString(end, "/") + CalenderConverter.DAY;
        Long interval = (endTime - startTime) / 200;
        List<Reading> readings = new ArrayList<>();
        for (String room : RoomConstants.rooms) {
            for (int i = 0; i < 200; i++) {
                if (i % 2 == 0) {
                    Reading reading = new Reading((i * interval) + startTime, 70, 40, room);
                    readings.add(reading);
                } else {
                    Reading reading = new Reading((i * interval) + startTime, 77, 51, room);
                    readings.add(reading);
                }
            }

        }
        readingRepository.saveAll(readings);
        latch.countDown();
        return true;
    }

    @Test
    public void whenGetHighLowRange_returnHighLow() {
        populateHighLowTestDB("12/01/2019", "1/07/2020");
        for (String room : RoomConstants.rooms) {
            List<String[]> hl = chartService.getHighLow("12/01/2019", "1/07/2020", room);
            assertEquals(38, hl.size());
            for (String[] strings : hl) {
                assertEquals(room + " Temp failed", "77-70", strings[0]);
                assertEquals(room + " Humidity failed", "51-40", strings[1]);
            }

        }
        readingRepository.truncateReadings();
    }

    @Test
    public void whenGenerateDiffChartData_diffChartData() {
        /*boolean setup = populateHighLowTestDB("12/01/2019", "12/07/2019");
        while(!setup){

        }
        DiffInterval[] hl = new DiffInterval[0];
        try {
            hl = chartService.generateDiffChartData(new ChartRequest("12/01/2019", "12/07/2019", "temp"));
        } catch (Exception e) {
            fail("Temp Test Threw Exception " + e.getMessage());
        }
        assertEquals(7, hl.length);
        int count = 0;
        for (DiffInterval diffInt : hl) {
            assertEquals("Temp Interval " + count, chartService.getDiffIntervalString("12/01/2019", count), diffInt.getName());
            assertEquals("Temp Bedroom " + count, "77-70", diffInt.getBedroom());
            assertEquals("Temp Livingroom " + count, "77-70", diffInt.getLivingRoom());
            assertEquals("Temp Serverroom " + count, "77-70", diffInt.getServerRoom());
            assertEquals("Temp Office " + count, "77-70", diffInt.getOffice());
            assertEquals("Temp Outside " + count, "77-70", diffInt.getOutside());
            assertEquals("Temp Heat " + count, "0-0", diffInt.getHeat());
            count++;
        }
        try {
            hl = chartService.generateDiffChartData(new ChartRequest("12/01/2019", "12/07/2019", "hum"));
        } catch (Exception e) {
            fail("Humidity Test Threw Exception " + e.getMessage());
        }
        assertEquals(7, hl.length);
        count = 0;
        for (DiffInterval diffInt : hl) {
            assertEquals("Humidity Interval " + count, chartService.getDiffIntervalString("12/01/2019", count), diffInt.getName());
            assertEquals("Humidity Bedroom " + count, "51-40", diffInt.getBedroom());
            assertEquals("Humidity Livingroom " + count, "51-40", diffInt.getLivingRoom());
            assertEquals("Humidity Serverroom " + count, "51-40", diffInt.getServerRoom());
            assertEquals("Humidity Office " + count, "51-40", diffInt.getOffice());
            assertEquals("Humidity Outside " + count, "51-40", diffInt.getOutside());
            assertEquals("Humidity Heat " + count, "51-40", diffInt.getHeat());
            count++;
        }
        readingRepository.truncateReadings();*/
    }
}

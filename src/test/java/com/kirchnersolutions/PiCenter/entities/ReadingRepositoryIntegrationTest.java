package com.kirchnersolutions.PiCenter.entities;

import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ReadingRepositoryIntegrationTest {

    @Autowired
    ApplicationContext context;
    @Autowired
    private ReadingRepository readingRepository;

    private Reading reading;
    private List<Reading> readings;

    @Before
    public void setUp(){
        readings = new ArrayList<>();
        reading = new Reading(System.currentTimeMillis(), 70, 50, "office");
        readings.add(reading);
        readings.add(new Reading(reading.getTime() + 1000, 75, 52, "office"));
        readings.add(new Reading(reading.getTime() + 2000, 72, 55, "office"));
        List<Reading> created = readingRepository.saveAll(readings);
        assertNotNull("create and save n user logs null", created);
        assertEquals("created <n readings", 3, created.size());
    }

    @Test
    public void whenFindByRoom_ReturnReadings(){
        List<Reading> found = readingRepository.findByRoomOrderByTimeDesc(reading.getRoom());
        assertNotNull("find readings by room null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByTime_ReturnReading(){
        List<Reading> found = readingRepository.findByTimeOrderByTimeDesc(reading.getTime());
        assertNotNull("find reading by room null", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByTimeLessThan_ReturnReadings(){
        List<Reading> found = readingRepository.findByTimeLessThanOrderByTimeDesc(reading.getTime() + 5000);
        assertNotNull("find readings null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByTimeLessThan_ReturnReading(){
        List<Reading> found = readingRepository.findByTimeLessThanOrderByTimeDesc(reading.getTime() + 100);
        assertNotNull("find reading null", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByTimeGreaterThan_ReturnReadings(){
        List<Reading> found = readingRepository.findByTimeGreaterThanOrderByTimeDesc(reading.getTime() - 1000);
        assertNotNull("find readings null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByGreaterLessThan_ReturnReading(){
        List<Reading> found = readingRepository.findByTimeGreaterThanOrderByTimeDesc(reading.getTime() + 1100);
        assertNotNull("find reading null", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByTimeAndRoom_ReturnReading(){
        List<Reading> found = readingRepository.findByTimeAndRoomOrderByTimeDesc(reading.getTime(), reading.getRoom());
        assertNotNull("find reading ", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByTimeBetween_ReturnReadings(){
        List<Reading> found = readingRepository.findByTimeBetweenOrderByTimeDesc(reading.getTime() - 1000, reading.getTime() + 5000);
        assertNotNull("find readings  null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByTimeBetweenAndRoom_ReturnReading(){
        List<Reading> found = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(reading.getTime() - 1000, reading.getTime() + 50, reading.getRoom());
        assertNotNull("find reading  null", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByTimeBetweenAndRoom_ReturnReadings(){
        List<Reading> found = readingRepository.findByTimeBetweenAndRoomOrderByTimeDesc(reading.getTime() - 1000, reading.getTime() + 5000, reading.getRoom());
        assertNotNull("find readings by time between null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByTimeBetween_ReturnReading(){
        List<Reading> found = readingRepository.findByTimeBetweenOrderByTimeDesc(reading.getTime() - 1000, reading.getTime() + 50);
        assertNotNull("find reading null", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByTempBetween_ReturnReadings(){
        List<Reading> found = readingRepository.findByTempBetweenOrderByTimeDesc(reading.getTemp() - 1, reading.getTemp() + 10);
        assertNotNull("find reading null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByTempBetween_ReturnReading(){
        List<Reading> found = readingRepository.findByTempBetweenOrderByTimeDesc(reading.getTemp() - 1, reading.getTemp() + 1);
        assertNotNull("find reading  null", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByTempBetweenAndRoom_ReturnReadings(){
        List<Reading> found = readingRepository.findByTempBetweenAndRoomOrderByTimeDesc(reading.getTemp() - 1, reading.getTemp() + 10, reading.getRoom());
        assertNotNull("find reading null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByTempBetweenAndRoom_ReturnReading(){
        List<Reading> found = readingRepository.findByTempBetweenAndRoomOrderByTimeDesc(reading.getTemp() - 1, reading.getTemp() + 1, reading.getRoom());
        assertNotNull("find reading  null", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByHumidityBetween_ReturnReadings(){
        List<Reading> found = readingRepository.findByHumidityBetweenOrderByTimeDesc(reading.getHumidity() - 1, reading.getHumidity() + 10);
        assertNotNull("find reading  null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByHumidityBetween_ReturnReading(){
        List<Reading> found = readingRepository.findByHumidityBetweenOrderByTimeDesc(reading.getHumidity() - 1, reading.getHumidity() + 1);
        assertNotNull("find reading null", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @Test
    public void whenFindByHumidityBetweenAndRoom_ReturnReadings(){
        List<Reading> found = readingRepository.findByHumidityBetweenAndRoomOrderByTimeDesc(reading.getHumidity() - 1, reading.getHumidity() + 10, reading.getRoom());
        assertNotNull("find reading null", found);
        assertEquals("found <n readings", 3, found.size());
    }

    @Test
    public void whenFindByHumidityBetweenAndRoom_ReturnReading(){
        List<Reading> found = readingRepository.findByHumidityBetweenAndRoomOrderByTimeDesc(reading.getHumidity() - 1, reading.getHumidity() + 1, reading.getRoom());
        assertNotNull("find reading", found);
        assertEquals("found >1 readings", 1, found.size());
    }

    @After
    public void cleanUp(){
        readingRepository.delete(reading);
        readings = null;
        reading = null;
    }
}

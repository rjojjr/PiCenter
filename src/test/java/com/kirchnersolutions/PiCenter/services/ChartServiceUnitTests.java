package com.kirchnersolutions.PiCenter.services;


import com.kirchnersolutions.PiCenter.entites.Reading;
import com.kirchnersolutions.PiCenter.entites.ReadingRepository;
import org.junit.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ChartServiceUnitTests {

    private ChartService chartService = new  ChartService(new StatService(mockReadingRepository()));

    @Test
    public void getTimeIntervals_returnsCorrectAmountOfIntervals(){
        List<Long> intervals = new ArrayList<>();
        intervals = chartService.getTimeIntervals("12/08/2019", "12/08/2019", 1);
        assertEquals(24, intervals.size());
        intervals = chartService.getTimeIntervals("12/08/2019", "12/08/2019", 3);
        assertEquals(8, intervals.size());
        intervals = chartService.getTimeIntervals("12/07/2019", "12/08/2019", 1);
        assertEquals(48, intervals.size());
    }

    private ReadingRepository mockReadingRepository(){
        return new ReadingRepository() {
            @Override
            public List<Reading> getAll() {
                return null;
            }

            @Override
            public List<Reading> findByTimeOrderByTimeDesc(Long time) {
                return null;
            }

            @Override
            public List<Reading> findByTimeAndRoomOrderByTimeDesc(Long time, String room) {
                return null;
            }

            @Override
            public List<Reading> findByRoomOrderByTimeDesc(String room) {
                return null;
            }

            @Override
            public List<Reading> findByTempBetweenOrderByTimeDesc(int start, int stop) {
                return null;
            }

            @Override
            public List<Reading> findByTempBetweenAndRoomOrderByTimeDesc(int start, int stop, String room) {
                return null;
            }

            @Override
            public List<Reading> findByHumidityBetweenOrderByTimeDesc(int start, int stop) {
                return null;
            }

            @Override
            public List<Reading> findByHumidityBetweenAndRoomOrderByTimeDesc(int start, int stop, String room) {
                return null;
            }

            @Override
            public List<Reading> findByTimeBetweenAndRoomOrderByTimeDesc(Long start, Long stop, String room) {
                return null;
            }

            @Override
            public List<Reading> findByTimeLessThanAndRoomOrderByTimeDesc(Long time, String room) {
                return null;
            }

            @Override
            public List<Reading> findByTimeGreaterThanAndRoomOrderByTimeDesc(Long time, String room) {
                return null;
            }

            @Override
            public List<Reading> findByTimeBetweenOrderByTimeDesc(Long start, Long stop) {
                return null;
            }

            @Override
            public List<Reading> findByTimeLessThanOrderByTimeDesc(Long time) {
                return null;
            }

            @Override
            public List<Reading> findByTimeGreaterThanOrderByTimeDesc(Long time) {
                return null;
            }

            @Override
            public void truncateReadings() {

            }

            @Override
            public List<Reading> findAll() {
                return null;
            }

            @Override
            public List<Reading> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Reading> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends Reading> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Reading> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public void deleteInBatch(Iterable<Reading> entities) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Reading getOne(Long aLong) {
                return null;
            }

            @Override
            public <S extends Reading> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Reading> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Reading> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Reading> S save(S entity) {
                return null;
            }

            @Override
            public Optional<Reading> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Reading entity) {

            }

            @Override
            public void deleteAll(Iterable<? extends Reading> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Reading> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Reading> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Reading> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Reading> boolean exists(Example<S> example) {
                return false;
            }
        };
    }

}

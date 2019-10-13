package com.kirchnersolutions.PiCenter.entites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingRepository extends CrudRepository<Reading, Long>, JpaRepository<Reading, Long> {

    List<Reading> findByTimeOrderByTimeDesc(Long time);

    List<Reading> findByTimeAndRoomOrderByTimeDesc(Long time, String room);

    List<Reading> findByRoomOrderByTimeDesc(String room);

    List<Reading> findByTempBetweenOrderByTimeDesc(int start, int stop);

    List<Reading> findByTempBetweenAndRoomOrderByTimeDesc(int start, int stop, String room);

    List<Reading> findByHumidityBetweenOrderByTimeDesc(int start, int stop);

    List<Reading> findByHumidityBetweenAndRoomOrderByTimeDesc(int start, int stop, String room);

    List<Reading> findByTimeBetweenAndRoomOrderByTimeDesc(Long start, Long stop, String room);

    List<Reading> findByTimeLessThanAndRoomOrderByTimeDesc(Long time, String room);

    List<Reading> findByTimeGreaterThanAndRoomOrderByTimeDesc(Long time, String room);

    List<Reading> findByTimeBetweenOrderByTimeDesc(Long start, Long stop);

    List<Reading> findByTimeLessThanOrderByTimeDesc(Long time);

    List<Reading> findByTimeGreaterThanOrderByTimeDesc(Long time);

}

package com.kirchnersolutions.PiCenter.entites;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadRepository extends CrudRepository<Read, Long> {

    List<Read> findByRoomOrderByTimeDesc(String room);

    List<Read> findByTimeBetweenAndRoomOrderByTimeDesc(Long start, Long stop, String room);

    List<Read> findByTimeLessThanAndRoomOrderByTimeDesc(Long time, String room);

    List<Read> findByTimeGreaterThanAndRoomOrderByTimeDesc(Long time, String room);

    List<Read> findByTimeBetweenOrderByTimeDesc(Long start, Long stop);

    List<Read> findByTimeLessThanOrderByTimeDesc(Long time);

    List<Read> findByTimeGreaterThanOrderByTimeDesc(Long time);

}

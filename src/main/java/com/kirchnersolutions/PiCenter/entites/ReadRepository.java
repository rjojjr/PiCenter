package com.kirchnersolutions.PiCenter.entites;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReadRepository extends CrudRepository<Read, Long> {

    List<Read> findByTimeBetween(Long start, Long stop);

    List<Read> findByTimeLessThan(Long time);

    List<Read> findByTimeGreaterThan(Long time);

}

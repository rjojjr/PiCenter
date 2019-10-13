package com.kirchnersolutions.PiCenter.entites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLogRepository extends CrudRepository<UserLog, Long>, JpaRepository<UserLog, Long> {

    List<UserLog> findByUserIdOrderByTimeDesc(Long userId);

}

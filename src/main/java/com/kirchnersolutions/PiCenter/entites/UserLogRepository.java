package com.kirchnersolutions.PiCenter.entites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLogRepository extends CrudRepository<UserLog, Long>, JpaRepository<UserLog, Long> {

    @Query("SELECT u FROM UserLog u")
    List<UserLog> getAll();

    List<UserLog> findByUserIdOrderByTimeDesc(Long userId);

    UserLog findByUserId(Long userId);

    @Modifying
    @Query(
            "delete from UserLog u"
    )
    void truncateUserLogs();

}

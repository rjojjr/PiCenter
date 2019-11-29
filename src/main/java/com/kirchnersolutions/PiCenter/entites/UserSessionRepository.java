package com.kirchnersolutions.PiCenter.entites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, Long>, JpaRepository<UserSession, Long> {

    @Query("SELECT u FROM UserSession u")
    List<UserSession> getAll();

    @Modifying
    @Query(
            "delete from UserSession u"
    )
    void truncateUserSessions();

}

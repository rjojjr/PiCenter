package com.kirchnersolutions.PiCenter.entites;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserNameAndPassword(String userName, String password);

    User findByUserName(String userName);

    List<User> findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    List<User> findByCreateTimeBetween(Long start, Long stop);

    List<User> findByCreateTimeLessThan(Long time);

    List<User> findByCreateTimeGreaterThan(Long time);
}

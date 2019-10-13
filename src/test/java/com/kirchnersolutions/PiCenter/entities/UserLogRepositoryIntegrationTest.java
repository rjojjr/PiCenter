package com.kirchnersolutions.PiCenter.entities;

import com.kirchnersolutions.PiCenter.entites.UserLog;
import com.kirchnersolutions.PiCenter.entites.UserLogRepository;
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

import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserLogRepositoryIntegrationTest {

    @Autowired
    ApplicationContext context;
    @Autowired
    private UserLogRepository userLogRepository;

    private UserLog userLog;

    @Before
    public void setUp() {
         userLog = new UserLog(Long.parseLong("1"), "created", System.currentTimeMillis());
         UserLog created = userLogRepository.saveAndFlush(userLog);
         assertNotNull("create user log null", created);
    }

    @Test
    public void whenFindByUserId_returnUserLog(){
        UserLog found = userLogRepository.findByUserIdOrderByTimeDesc(userLog.getUserId()).get(0);
        assertNotNull("find user log by user id null", found);
    }

    @After
    public void cleanUp(){
        userLogRepository.delete(userLog);
        userLog = null;
    }

}

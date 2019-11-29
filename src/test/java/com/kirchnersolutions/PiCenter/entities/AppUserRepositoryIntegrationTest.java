package com.kirchnersolutions.PiCenter.entities;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import com.kirchnersolutions.PiCenter.entites.AppUserRepository;
import com.kirchnersolutions.utilities.CryptTools;
import lombok.Cleanup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.kirchnersolutions.PiCenter.entites.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class AppUserRepositoryIntegrationTest {

    @Autowired
    ApplicationContext context;
    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser user;

    @Before
    public void setUp() throws Exception{
        user = new AppUser((Long)System.currentTimeMillis(), "rjojjr", "Robert", "Kirchner", CryptTools.generateEncodedSHA256Password("21122112"), true);
        AppUser created = appUserRepository.saveAndFlush(user);
        assertNotNull("create and save user null", created);
    }

    @Test
    public void whenFindAll_returnAll(){
        List<AppUser> found = appUserRepository.findAll();
        assertNotNull("find all null", found);
        assertEquals(1, found.size());
    }

    @Test
    public void whenFindByUsername_returnUser(){
        AppUser found = appUserRepository.findByUserName("rjojjr");
        assertNotNull("find user by username null", found);
    }

    @Test
    public void whenGetUserIdByUsername_returnId(){
        Long found = appUserRepository.getUserIdByUserName("rjojjr");
        assertNotNull("find user id by username null", found);
        assertNotEquals("user id equals -1", 0, found.compareTo((Long.parseLong("-1"))));
    }

    @Test
    public void whenFindByUsernameAndPassword_returnUser(){
        AppUser found = appUserRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword()).get(0);
        assertNotNull("find user by username and password", found);
    }

    @After
    public void cleanUp(){
        appUserRepository.delete(user);
        user = null;
    }
}

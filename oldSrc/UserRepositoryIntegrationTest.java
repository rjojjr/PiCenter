package com.kirchnersolutions.PiCenter;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.kirchnersolutions.PiCenter.entites.User;
import com.kirchnersolutions.PiCenter.entites.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    User user;

    @Before
    public void setUp() {
        user = new User(System.currentTimeMillis(), "rjojjr", "Robert", "Kirchner", "21122112", true);
    }

    @Test
    public void saveUser(){
        User savedUser = userRepository.save(user);
        assertEquals(savedUser.getFirstName(), "Robert");
    }

    @Test
    public void whenFindByUsername_returnUser(){
        User found = userRepository.findByUserName(user.getUserName());
        assertEquals(found.getFirstName(), ("Robert"));
    }

    @Test
    public void whenFindByUsernameAndPassword_returnUser(){
        User found = userRepository.findByUserNameAndPassword(user.getUserName(), user.getPassword());
        assertEquals(found.getFirstName(), ("Robert"));
    }

    @Test
    public void deleteUser(){
        userRepository.remove(user);
        assertNull(userRepository.findByUserName(user.getUserName()));
    }
}

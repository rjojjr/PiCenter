package com.kirchnersolutions.PiCenter.services;

/**
 * PiCenter: Raspberry Pi home automation control center.
 *
 * Copyright (C) 2019  Robert Kirchner JR
 *
 *         This program is free software: you can redistribute it and/or modify
 *         it under the terms of the GNU Affero General Public License as
 *         published by the Free Software Foundation, either version 3 of the
 *         License, or (at your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *         GNU Affero General Public License for more details.
 *
 *         You should have received a copy of the GNU Affero General Public License
 *         along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.dev.exceptions.UserRoleException;
import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.PiCenter.servers.beans.RestUser;
import com.kirchnersolutions.PiCenter.services.UserService;
import com.kirchnersolutions.utilities.CryptTools;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

@DependsOn({"userService"})
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {MainConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserListTest {

    @Autowired
    private UserList userList;

    @Before
    public void setUp(){
        for(int i = 0; i < 10; i++){
            try{
                userList.addUser(new AppUser(System.currentTimeMillis(), "admin" + i, "Robert", "Kirchner Jr", CryptTools.generateEncodedSHA256Password("password"), true));
            }catch (Exception e){

            }
        }
    }

    @Test
    public void findUserFromList(){
        assertNotNull("didn't find user", userList.searchList("admin0"));
    }

    @Test
    public void removeUserFromList(){
        AppUser admin0 = userList.searchList("admin0");
        assertNotNull("didn't find user", admin0);
        userList.removeUser(admin0.getUserName());
        admin0 = userList.searchList("admin0");
        assertNull("didn't remove user", admin0);
    }

    @After
    public void cleanUp(){
        userList.dumpList();
    }

}

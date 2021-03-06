package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.dev.exceptions.UserRoleException;
import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.PiCenter.servers.beans.RestUser;
import com.kirchnersolutions.PiCenter.services.UserService;
import com.kirchnersolutions.utilities.CryptTools;
import org.junit.After;
import org.junit.Before;
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

//@SpringBootTest
@DependsOn({"userService"})
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {MainConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceIntegrationTest {

    private UserService userService;

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired(required=true)
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    private AppUser testUser, admin;

    private static String adminPassword = "21122112", testPassword = "testUser#";

    private void resetUsers() throws Exception{
        admin = new AppUser(System.currentTimeMillis(), "admin", "Robert", "Kirchner Jr", CryptTools.generateEncodedSHA256Password(adminPassword), true);
        testUser = new AppUser(System.currentTimeMillis(), "TestUser", "Test", "User", CryptTools.generateEncodedSHA256Password(testPassword), false);
    }

    @Before
    public void setUp() throws Exception {
        //userService = new UserService();
        admin = new AppUser(System.currentTimeMillis(), "admin", "Robert", "Kirchner Jr", CryptTools.generateEncodedSHA256Password(adminPassword), true);
        testUser = new AppUser(System.currentTimeMillis(), "TestUser", "Test", "User", CryptTools.generateEncodedSHA256Password(testPassword), false);
        admin = appUserRepository.saveAndFlush(admin);
        testUser = appUserRepository.saveAndFlush(testUser);
        assertNotNull(" admin null", admin);
    }

    @Test
    public void whenLogOnNotExistUser_ReturnNull() throws Exception{
        assertNull("find user that does not exist not null", userService.logOn("TestUser2", "dne", "null"));
    }

    @Test
    public void whenLogOnExistUser_ReturnUser() throws Exception{
        //testUser2 = userService.logOn("TestUser2", CryptTools.generateEncodedSHA256Password("testUser2#"), "null");
        testUser = userService.logOn("TestUser", testPassword, "null");
        assertNotNull("log on existing testUser null", testUser);
        testUser = userService.logOn("TestUser", testPassword, "null");
        assertNotNull("log on existing testUser null", testUser);
        userService.logOff(testUser.getUserName());
    }

    @Test
    public void whenLoggedIn_ReturnDefaultSession() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        assertNotNull("admin user logon null", admin);
        UserSession userSession = admin.getUserSession();
        assertNotNull("admin user session null", userSession);
        assertNotNull("admin user session token null", userSession.getToken());
        assertEquals("admin session page not equal '/summary'","/summary", userSession.getPage());
        assertEquals("admin session ip not equal 'null'","null", userSession.getIpAddress());
        assertNull("admin user session stomp id not null", userSession.getStompId());
        userService.logOff(admin.getUserName());
    }

    @Test
    public void whenUpdateSession_ReturnNotEqualToDefaultSession() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        assertNotNull("admin user logon null", admin);
        UserSession userSession = admin.getUserSession();
        assertNotNull("admin user session null", userSession);
        admin = userService.updateSession("admin", admin.getUserSession().getToken(), "192.168.1.168", "/test", "stomp");
        assertNotNull("admin user session token null", userSession.getToken());
        assertEquals("admin session page equal '/'","/test", userSession.getPage());
        assertNotEquals("admin session ip equal 'null'","null", userSession.getIpAddress());
        assertNotNull("admin session stomp id equal null",userSession.getStompId());
        admin = userService.updateSession("admin", admin.getUserSession().getToken(), "192.168.1.168", "/test");
        assertNotNull("admin user session token null", userSession.getToken());
        assertEquals("admin session page equal '/'","/test", userSession.getPage());
        assertNotEquals("admin session ip equal 'null'","null", userSession.getIpAddress());
        assertNotNull("admin session stomp id equal null",userSession.getStompId());
        userService.logOff(admin.getUserName());
    }

    @Test
    public void whenAddUserByAdmin_ReturnTrue() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        assertNotNull("admin user logon null", admin);
        assertTrue("create testuser2 by admin user false", userService.addUser(admin, "TestUser2", "Test", "User2", CryptTools.generateEncodedSHA256Password("testUser"), true));
        userService.logOff(admin.getUserName());
    }

    @Test(expected = UserRoleException.class)
    public void whenInvalidateUserByNonAdmin_ThrowUserRoleException() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        assertNotNull("admin user logon null", admin);
        testUser = userService.logOn(testUser.getUserName(), testPassword, "null");
        assertNotNull("test user logon null", testUser);
        userService.invalidateUser(testUser, admin.getUserName());
        userService.logOff(admin.getUserName());
        userService.logOff(testUser.getUserName());
    }

    @Test
    public void whenInvalidateUserThatDoesNotExistByAdmin_ReturnFalse() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        assertNotNull("admin user logon null", admin);
        assertFalse("invalidate non exist user true", userService.invalidateUser(admin, "dne"));
        userService.logOff(admin.getUserName());
    }

    @Test
    public void whenInvalidateUserByAdmin_ReturnTrue() throws Exception{
        //resetUsers();
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        assertNotNull("admin user logon null", admin);
        testUser = userService.logOn(testUser.getUserName(), testPassword, "null");
        assertNotNull("test user logon null", testUser);
        assertTrue("invalidate user false", userService.invalidateUser(admin, testUser.getUserName()));
        userService.logOff(admin.getUserName());
    }

    @Test
    public void whenLoggedOn_LogOffReturnTrue() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        assertNotNull("admin user logon null", admin);
        assertTrue("failed to log admin user out",userService.logOff(admin.getUserName()));
    }

    @Test
    public void whenNotLoggedOn_LogOffReturnFalse() throws Exception{
        assertFalse("returned true",userService.logOff("Robert"));
    }

    @Test(expected = UserRoleException.class)
    public void whenDeleteUserByNonAdmin_ThrowUserRoleException() throws Exception{
        testUser = userService.logOn(testUser.getUserName(), testPassword, "null");
        assertNotNull("test user logon null", testUser);
        userService.removeUser(testUser, admin.getUserName());
        userService.logOff(testUser.getUserName());
    }

    @Test
    public void whenDeleteUserByAdmin_ReturnTrue() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        assertNotNull("admin user logon null", admin);
        assertTrue("delete user by admin false", userService.removeUser(admin, "TestUser"));
        testUser = new AppUser(System.currentTimeMillis(), "TestUser", "Test", "User", CryptTools.generateEncodedSHA256Password("testUser#"), false);
        //testUser = appUserRepository.saveAndFlush(testUser);
        userService.logOff(admin.getUserName());
        //admin = null;
    }

    @Test
    public void whenGetRestUserByNotLoggedOnUser_ReturnDefaultRestUser() throws Exception{
        RestUser restUser = userService.getRestUser("null");
        assertTrue("userName != null", restUser.getUserName().equals(new RestUser().getUserName()));
        assertTrue("token != null", restUser.getToken().equals(new RestUser().getToken()));
        assertTrue("page != /login", restUser.getPage().equals(new RestUser().getPage()));
        assertTrue("ip != null", restUser.getIp().equals(new RestUser().getIp()));
        assertTrue("stompId != null", restUser.getStompId().equals(new RestUser().getStompId()));
    }

    @Test
    public void whenGetRestUserByLoggedOnUser_ReturnProperRestUser() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        RestUser restUser = userService.getRestUser(admin.getUserName());
        assertTrue("userName = null", restUser.getUserName().equals(admin.getUserName()));
        assertTrue("token = null", admin.getUserSession().getToken().compareTo(restUser.getToken()) == 0);
        assertTrue("page = /login", restUser.getPage().equals(admin.getUserSession().getPage()));
        assertTrue("ip = null", restUser.getIp().equals(admin.getUserSession().getIpAddress()));
        assertTrue("stompId = null", restUser.getStompId().equals("null"));
        userService.logOff(admin.getUserName());
    }

    @Test
    public void whenGetRestPasswordByAdmin_ReturnProperUser() throws Exception{
        admin = userService.logOn(admin.getUserName(), adminPassword, "null");
        RestUser restUser = userService.getRestUser(admin.getUserName());
        assertTrue("userName = null", restUser.getUserName().equals(admin.getUserName()));
        assertTrue("token = null", admin.getUserSession().getToken().compareTo(restUser.getToken()) == 0);
        assertTrue("page = /login", restUser.getPage().equals(admin.getUserSession().getPage()));
        assertTrue("ip = null", restUser.getIp().equals(admin.getUserSession().getIpAddress()));
        assertTrue("stompId = null", restUser.getStompId().equals("null"));
        userService.logOff(admin.getUserName());
    }

   @After
    public void cleanUp()throws Exception{
        appUserRepository.delete(admin);
        appUserRepository.delete(testUser);
    }
}

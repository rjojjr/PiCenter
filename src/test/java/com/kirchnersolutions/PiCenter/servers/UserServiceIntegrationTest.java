package com.kirchnersolutions.PiCenter.servers;

import com.kirchnersolutions.PiCenter.MainConfig;
import com.kirchnersolutions.PiCenter.dev.exceptions.UserRoleException;
import com.kirchnersolutions.PiCenter.entites.AppUser;
import com.kirchnersolutions.PiCenter.entites.AppUserRepository;
import com.kirchnersolutions.PiCenter.entites.UserLog;
import com.kirchnersolutions.PiCenter.entites.UserSession;
import com.kirchnersolutions.PiCenter.test.config.UserServiceTestConfig;
import com.kirchnersolutions.utilities.CryptTools;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

@DependsOn({"userService"})
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {MainConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceIntegrationTest {

    private UserService userService;

    //@Autowired
    //ApplicationContext context;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired(required=true)
    public void setUserService(UserService userService){
        this.userService = userService;
    }



    private AppUser testUser, admin, testUser2;

    @Before
    public void setUp() throws Exception {
        admin = new AppUser(System.currentTimeMillis(), "admin", "Robert", "Kirchner Jr", CryptTools.generateEncodedSHA256Password("21122112"), true);
        testUser = new AppUser(System.currentTimeMillis(), "TestUser", "Test", "User", CryptTools.generateEncodedSHA256Password("testUser#"), false);
        admin = appUserRepository.saveAndFlush(admin);
        testUser = appUserRepository.saveAndFlush(testUser);
        admin = userService.logOn(admin.getUserName(), admin.getPassword(), "null");
        assertNotNull("log on existing admin null", admin);
    }

    @Test
    public void whenLogOnNotExistUser_ReturnNull() throws Exception{
        assertNull("find user that does not exist not null", userService.logOn("TestUser2", "dne", "null"));
    }

    @Test
    public void whenLogOnExistUser_ReturnUser() throws Exception{
        testUser2 = userService.logOn("TestUser2", CryptTools.generateEncodedSHA256Password("testUser2#"), "null");
        testUser = userService.logOn("TestUser1", CryptTools.generateEncodedSHA256Password("testUser#"), "null");
        assertNotNull("log on existing testUser2 null", testUser2);
        assertNotNull("log on existing testUser null", testUser);
    }

    @Test
    public void whenLoggedIn_ReturnDefaultSession() throws Exception{
        UserSession userSession = admin.getUserSession();
        assertNotNull("admin user session null", userSession);
        assertNotNull("admin user session token null", userSession.getToken());
        assertEquals("admin session page not equal '/'","/test", userSession.getPage());
        //assertEquals("admin session ip not equal 'null'","null", userSession.getIpAddress());
        assertNull("admin user session stomp id not null", userSession.getStompId());
    }

    @Test
    public void whenUpdateSession_ReturnNotEqualToDefaultSession() throws Exception{
        UserSession userSession = admin.getUserSession();
        assertNotNull("admin user session null", userSession);
        admin = userService.updateSession("admin", admin.getUserSession().getToken(), "192.168.1.168", "/test", "stomp");
        assertNotEquals("admin session page equal '/'","/", userSession.getPage());
        assertNotEquals("admin session ip equal 'null'","null", userSession.getIpAddress());
        assertNotNull("admin session stomp id equal null",userSession.getStompId());
    }

    @Test
    public void whenAddUserByAdmin_ReturnTrue() throws Exception{
        assertTrue("create testuser2 by admin user false", userService.addUser(admin, "TestUser2", "Test", "User2", CryptTools.generateEncodedSHA256Password("testUser2#"), true));
    }

    @Test(expected = UserRoleException.class)
    public void whenInvalidateUserByNonAdmin_ThrowUserRoleException() throws Exception{
        userService.invalidateUser(testUser, testUser2.getUserName());
    }

    @Test
    public void whenInvalidateUserThatDoesNotExistByAdmin_ReturnFalse() throws Exception{
        assertFalse("invalidate non exist user true", userService.invalidateUser(admin, "dne"));
    }

    @Test
    public void whenInvalidateUserByAdmin_ReturnTrue() throws Exception{
        assertTrue("invalidate user false", userService.invalidateUser(admin, testUser2.getUserName()));
    }

    @Test(expected = UserRoleException.class)
    public void whenDeleteUserByNonAdmin_ThrowUserRoleException() throws Exception{
        userService.removeUser(testUser, testUser2.getUserName());
    }

    @Test
    public void whenDeleteUserByAdmin_ReturnTrue() throws Exception{
        assertTrue("delete user by admin false", userService.removeUser(admin, testUser2.getUserName()));
    }

}

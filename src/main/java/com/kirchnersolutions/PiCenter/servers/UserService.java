package com.kirchnersolutions.PiCenter.servers;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.dev.DevelopmentException;
import com.kirchnersolutions.PiCenter.dev.exceptions.UserRoleException;
import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.utilities.CryptTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Base64;
import java.util.List;

@Service
@DependsOn({"debuggingService", "appUserRepository", "userLogRepository", "userList"})
public class UserService {

    public static final boolean TEST = true;

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserList userList;
    @Autowired
    private DebuggingService debuggingService;
    @Autowired
    private UserLogRepository userLogRepository;

  /*  private AppUser testUser = null, admin = null, testUser2 = null;

    public void testInit() throws Exception{
        if(TEST){
            admin = new AppUser(System.currentTimeMillis(), "admin", "Robert", "Kirchner Jr", CryptTools.generateEncodedSHA256Password("21122112"), true);
            testUser = new AppUser(System.currentTimeMillis(), "TestUser", "Test", "User", CryptTools.generateEncodedSHA256Password("testUser#"), false);
            admin = appUserRepository.saveAndFlush(admin);
            testUser = appUserRepository.saveAndFlush(testUser);
            admin = logOn(admin.getUserName(), admin.getPassword(), "null");
        }
    }*/

    boolean addUser(AppUser creator, String userName, String firstName, String lastName, String password, boolean admin) throws Exception{
        if(!creator.isAdmin()){
            throw new UserRoleException("Only admin can create user");
        }
        password = CryptTools.generateEncodedSHA256Password(password);
        if(appUserRepository.findByUserNameAndPassword(userName, password) != null){
            return false;
        }
        AppUser user = new AppUser(System.currentTimeMillis(), userName, firstName, lastName, password, admin);
        user = appUserRepository.saveAndFlush(user);
        UserLog userLog = new UserLog(user.getId(), "created", System.currentTimeMillis());
        userLogRepository.saveAndFlush(userLog);
        UserLog creatorLog = new UserLog(creator.getId(), "created user " + userName, System.currentTimeMillis());
        userLogRepository.saveAndFlush(creatorLog);
        return true;
    }

    boolean removeUser(AppUser creator, String userName) throws Exception{
        AppUser user;
        if(!creator.isAdmin()){
            throw new UserRoleException("Only admin can create user");
        }
        if((user = appUserRepository.findByUserName(userName)) == null){
            return false;
        }
        UserLog userLog = new UserLog(user.getId(), "deleted", System.currentTimeMillis());
        userLogRepository.saveAndFlush(userLog);
        userList.removeUser(userName);
        appUserRepository.delete(user);
        UserLog creatorLog = new UserLog(creator.getId(), "deleted user " + userName, System.currentTimeMillis());
        userLogRepository.saveAndFlush(creatorLog);
        return true;
    }

    boolean invalidateUser(AppUser admin, String userName) throws Exception{
        AppUser user;
        if(!admin.isAdmin()){
            throw new UserRoleException("Only admin can create user");
        }
        if((user = appUserRepository.findByUserName(userName)) == null){
            return false;
        }
        UserLog userLog = new UserLog(user.getId(), "invalidated by " + admin.getUserName(), System.currentTimeMillis());
        userLogRepository.saveAndFlush(userLog);
        userList.removeUser(userName);
        user.setUserSession(null);
        appUserRepository.saveAndFlush(user);
        UserLog creatorLog = new UserLog(admin.getId(), "invalidated user " + userName, System.currentTimeMillis());
        userLogRepository.saveAndFlush(creatorLog);
        return true;
    }

    AppUser updateSession(String userName, BigInteger token, String ipAddress, String page, String stompId){
        AppUser user = userList.searchList(userName);
        if(user == null){
            return null;
        }
        UserSession userSession = user.getUserSession();
        if(userSession.getToken().compareTo(token) != 0){
            return null;
        }
        userSession.setExpirationTime(getExpirationTime());
        userSession.setIpAddress(ipAddress);
        userSession.setPage(page);
        userSession.setStompId(stompId);
        appUserRepository.saveAndFlush(user);
        return user;
    }

    AppUser updateSession(String userName, BigInteger token, String ipAddress, String page){
        AppUser user = userList.searchList(userName);
        if(user == null){
            return null;
        }
        UserSession userSession = user.getUserSession();
        if(userSession.getToken().compareTo(token) != 0){
            return null;
        }
        userSession.setExpirationTime(getExpirationTime());
        userSession.setIpAddress(ipAddress);
        userSession.setPage(page);
        appUserRepository.saveAndFlush(user);
        return user;
    }

    AppUser logOn(String userName, String password, String ipAddress) throws Exception{
        AppUser user = userList.searchList(userName);
        //password = CryptTools.generateEncodedSHA256Password(password);
        if(user != null){
            return user;
        }
        try{
            //Check not null
            //user = appUserRepository.findByUserNameAndPassword(userName, CryptTools.generateEncodedSHA256Password(password)).get(0);
            user = appUserRepository.findByUserNameAndPassword(userName, password).get(0);
            if(user == null){
                return null;
            }
            if(!userList.addUser(user)){
                debuggingService.throwDevException(new DevelopmentException("Failed to add user " + userName + " to user list"));
                debuggingService.nonFatalDebug("Failed to add user " + userName + " to user list");
                return null;
            }
            UserLog userLog = new UserLog(user.getId(), "logon", System.currentTimeMillis());
            userLogRepository.saveAndFlush(userLog);
            Long time = System.currentTimeMillis();
            UserSession userSession = new UserSession(time, getExpirationTime(), createToken(userName, password, ipAddress), "/", ipAddress);
            user.setUserSession(userSession);
            //appUserRepository.saveAndFlush(user);
            return user;
        }catch (Exception e){

            //debuggingService.throwDevException(new DevelopmentException("Failed to process password for user " + userName));
            throw e;
            //debuggingService.nonFatalDebug("Failed to process password for user " + userName);
            //return null;
        }
    }

    boolean logOff(String userName){
        AppUser user = userList.searchList(userName);
        if(user == null){
            return false;
        }else{
            UserLog userLog = new UserLog(user.getId(), "logoff", System.currentTimeMillis());
            userLogRepository.saveAndFlush(userLog);
            user.setUserSession(null);
            appUserRepository.saveAndFlush(user);
            userList.removeUser(user.getUserName());
            user = null;
            return true;
        }
    }

    @Scheduled(fixedDelay = 60000)
    private void bootExpired(){
        List<AppUser> expiredUsers = userList.searchExpired();
        for(AppUser user : expiredUsers){
            userList.removeUser(user.getUserName());
            UserLog userLog = new UserLog(user.getId(), "expired", System.currentTimeMillis());
            userLogRepository.saveAndFlush(userLog);
            user.setUserSession(null);
            appUserRepository.saveAndFlush(user);
            user = null;
        }
    }

    private static BigInteger createToken(String userName, String password, String ipAddress) throws Exception{
        byte[] rand;
        byte[] hash = CryptTools.getSHA256(Base64.getEncoder().encode(userName.getBytes()), Base64.getEncoder().encode(password.getBytes()));
        hash = CryptTools.getSHA256(hash, Base64.getEncoder().encode(ipAddress.getBytes()));
        for(int i = 0; i < 1000; i++){
            rand = CryptTools.generateRandomBytes(1024 * 1024);
            hash = CryptTools.getSHA256(hash, rand);
        }
        return new BigInteger(hash);
    }

    private static Long getExpirationTime(){
        return System.currentTimeMillis() + (1000 * 60 * 30);
    }

}

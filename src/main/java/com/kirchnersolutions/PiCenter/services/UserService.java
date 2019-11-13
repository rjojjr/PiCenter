package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.dev.DevelopmentException;
import com.kirchnersolutions.PiCenter.dev.exceptions.UserRoleException;
import com.kirchnersolutions.PiCenter.entites.*;
import com.kirchnersolutions.PiCenter.servers.UserList;
import com.kirchnersolutions.PiCenter.servers.beans.RestUser;
import com.kirchnersolutions.utilities.CryptTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    @Autowired
    private UserSessionRepository userSessionRepository;

    public boolean addUser(AppUser creator, String userName, String firstName, String lastName, String password, boolean admin) throws Exception{
        if(!creator.isAdmin()){
            throw new UserRoleException("Only admin can create user");
        }
        //password = CryptTools.generateEncodedSHA256Password(password);
        if(appUserRepository.findByUserName(userName) != null){
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

    public boolean removeUser(AppUser creator, String userName) throws Exception{
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

    public boolean invalidateUser(AppUser admin, String userName) throws Exception{
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
        user.setUserSession(new UserSession(null, null, null, null, null));
        //appUserRepository.saveAndFlush(user);
        UserLog creatorLog = new UserLog(admin.getId(), "invalidated user " + userName, System.currentTimeMillis());
        userLogRepository.saveAndFlush(creatorLog);
        return true;
    }

    public boolean systemInvalidateUser(String userName, String msg) throws Exception{
        AppUser user;
        if((user = appUserRepository.findByUserName(userName)) == null){
            return false;
        }
        UserLog userLog = new UserLog(user.getId(), "invalidated by system: " + msg, System.currentTimeMillis());
        userLogRepository.saveAndFlush(userLog);
        userList.removeUser(userName);
        user.setUserSession(new UserSession(null, null, null, null, null));
        return true;
    }

    public AppUser updateSession(String userName, String token, String ipAddress, String page, String stompId){
        AppUser user = userList.searchList(userName);
        if(user == null){
            return null;
        }
        UserSession userSession = user.getUserSession();
        if(!userSession.getToken().equals(token)){
            return null;
        }
        userSession.setExpirationTime(getExpirationTime());
        userSession.setIpAddress(ipAddress);
        userSession.setPage(page);
        userSession.setStompId(stompId);
        user.setUserSession(userSession);
        //appUserRepository.saveAndFlush(user);
        return user;
    }

    public AppUser updateSession(String userName, String token, String ipAddress){
        AppUser user = userList.searchList(userName);
        if(user == null){
            return null;
        }
        UserSession userSession = user.getUserSession();
        if(!userSession.getToken().equals(token)){
            return null;
        }
        userSession.setExpirationTime(getExpirationTime());
        userSession.setIpAddress(ipAddress);
        user.setUserSession(userSession);
        //appUserRepository.saveAndFlush(user);
        return user;
    }

    public AppUser updateSession(String userName, String token, String ipAddress, String page){
        AppUser user = userList.searchList(userName);
        if(user == null){
            return null;
        }
        UserSession userSession = user.getUserSession();
        if(!userSession.getToken().equals(token)){
            return null;
        }
        userSession.setExpirationTime(getExpirationTime());
        userSession.setIpAddress(ipAddress);
        userSession.setPage(page);
        user.setUserSession(userSession);
        //appUserRepository.saveAndFlush(user);
        return user;
    }

    public AppUser logOn(String userName, String password, String ipAddress) throws Exception{
        AppUser user = userList.searchList(userName);

        if(user != null){
            return user;
        }
        try{
            //Check not null
            //user = appUserRepository.findByUserNameAndPassword(userName, CryptTools.generateEncodedSHA256Password(password)).get(0);

            List<AppUser> results = appUserRepository.findByUserNameAndPassword(userName, CryptTools.generateEncodedSHA256Password(password));
            if(results.isEmpty()){
                return null;
            }
            user = results.get(0);
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
            UserSession userSession = new UserSession(time, getExpirationTime(), createToken(userName, password, ipAddress), "/summary", ipAddress);
            user.setUserSession(userSession);
            //appUserRepository.saveAndFlush(user);
            return user;
        }catch (Exception e){
            debuggingService.throwDevException("Failed to process password for user ", e);
            debuggingService.nonFatalDebug("Failed to process password for user " + userName, e);
            return null;
        }
    }

    public boolean logOff(String userName){
        AppUser user = userList.searchList(userName);
        if(user == null){
            return false;
        }else{
            UserLog userLog = new UserLog(user.getId(), "logoff", System.currentTimeMillis());
            userLogRepository.saveAndFlush(userLog);
            userSessionRepository.delete(user.getUserSession());
            //appUserRepository.saveAndFlush(user);
            userList.removeUser(user.getUserName());
            user = null;
            return true;
        }
    }

    public RestUser getRestUser(String username){
        AppUser user = userList.searchList(username);
        return restUserFactory(user);
    }

    @Scheduled(fixedDelay = 60000)
    private void bootExpired(){
        List<AppUser> expiredUsers = userList.searchExpired();
        for(AppUser user : expiredUsers){
            userList.removeUser(user.getUserName());
            UserLog userLog = new UserLog(user.getId(), "expired", System.currentTimeMillis());
            userLogRepository.saveAndFlush(userLog);
            user.setUserSession(new UserSession(null, null, null, null, null));
            //appUserRepository.saveAndFlush(user);
            user = null;
        }
    }

    private static String createToken(String userName, String password, String ipAddress) throws Exception{
        byte[] rand;
        byte[] hash = CryptTools.getSHA256(Base64.getEncoder().encode(userName.getBytes()), Base64.getEncoder().encode(password.getBytes()));
        hash = CryptTools.getSHA256(hash, Base64.getEncoder().encode(ipAddress.getBytes()));
        for(int i = 0; i < 1000; i++){
            rand = CryptTools.generateRandomBytes(1024);
            hash = CryptTools.getSHA256(hash, rand);
        }
        String temp = Base64.getEncoder().encodeToString(hash);

        return temp.replaceAll("/", "s").replaceAll("\\.", "pE").replaceAll("=", "EQ").replaceAll(",", "Co").replaceAll("\\+", "pl");
    }

    private static RestUser restUserFactory(AppUser user){
        if(user == null){
            return new RestUser();
        }
        UserSession session = user.getUserSession();
        String token = session.getToken();
        String page = session.getPage();
        String ip = session.getIpAddress();
        String stompId = session.getStompId();
        if(token == null){
            token = "null";
        }
        if(page == null){
            page = "null";
        }
        if(ip == null){
            ip = "null";
        }
        if(stompId == null){
            stompId = "null";
        }
        return new RestUser(user.getUserName(), token, page, ip, stompId);
    }

    private static Long getExpirationTime(){
        return System.currentTimeMillis() + (1000 * 60 * 30);
    }

}

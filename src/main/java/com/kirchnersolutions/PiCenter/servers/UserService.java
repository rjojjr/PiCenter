package com.kirchnersolutions.PiCenter.servers;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.dev.DevelopmentException;
import com.kirchnersolutions.PiCenter.entites.User;
import com.kirchnersolutions.PiCenter.entites.UserRepository;
import com.kirchnersolutions.PiCenter.servers.objects.UserList;
import com.kirchnersolutions.utilities.CryptTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private UserList userList;
    @Autowired
    private DebuggingService debuggingService;

    //"methodName,param1;param2"
    //private Queue<String> queue;

    public UserService(){
        //queue = new LinkedList<>();
    }

    User logOn(String userName, String password) throws Exception{
        User user = userList.searchList(userName);
        if(user != null){
            return user;
        }
        try{
            user = userRepository.findByUserNameAndPassword(userName, CryptTools.generateEncodedSHA256Password(password));
            if(user == null){
                return null;
            }
            if(!userList.addUser(user)){
                debuggingService.throwDevException(new DevelopmentException("Failed to add user " + userName + " to user list"));
                debuggingService.nonFatalDebug("Failed to add user " + userName + " to user list");
                return null;
            }
            return user;
        }catch (Exception e){
            debuggingService.throwDevException(new DevelopmentException("Failed to process password for user " + userName));
            debuggingService.nonFatalDebug("Failed to process password for user " + userName);
            return null;
        }
    }

}

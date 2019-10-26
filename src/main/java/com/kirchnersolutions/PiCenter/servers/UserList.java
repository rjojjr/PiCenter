package com.kirchnersolutions.PiCenter.servers;

import com.kirchnersolutions.PiCenter.entites.AppUser;
import com.kirchnersolutions.PiCenter.entites.UserSession;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UserList {

    private List<AppUser> userList;
    private AtomicBoolean lock = new AtomicBoolean(false);

    public UserList(){
        userList = Collections.synchronizedList(new ArrayList<>());
    }


    public List<AppUser> searchExpired(){
        Long time = System.currentTimeMillis();
        List<AppUser> expiredUsers = new ArrayList<>();
        for(AppUser user : copyList()){
            UserSession userSession = user.getUserSession();
            if(userSession.getExpirationTime() <= time){
                expiredUsers.add(user);
            }
        }
        return expiredUsers;
    }


    public synchronized boolean addUser(AppUser user){
        while(get()){

        }
        set(true);
        boolean result = false;
        List<AppUser> list = copyList();
        if(searchList(user.getUserName(), list) == null){
            list.add(user);
            updateList(list);
            result = true;
        }
        set(false);
        return result;
    }

    public synchronized AppUser searchList(String userName){
        while(get()){

        }
        set(true);
        AppUser user = searchList(userName, copyList());
        set(false);
        return user;
    }

    public synchronized boolean removeUser(String userName){
        while(get()){

        }
        set(true);
        boolean result = false;
        List<AppUser> list = copyList();
        List<AppUser> appUserList = remove(userName, list);
        if(appUserList != null){
            updateList(appUserList);
            result = true;
        }
        set(false);
        return result;
    }

    private List<AppUser> remove(String userName, List<AppUser> list){
        List<AppUser> newList = new ArrayList<>();
        boolean result = false;
        for(AppUser user : list){
            if(!user.getUserName().equals(userName)){
                newList.add(user);
            }else{
                result = true;
            }
        }
        if(result){
            return new ArrayList<>(newList);
        }
        return null;
    }

    private static AppUser searchList(String userName, List<AppUser> list){
        for(AppUser user : list){
            if(user.getUserName().equals(userName)){
                return user;
            }
        }
        return null;
    }

    private synchronized boolean get(){
        return lock.get();
    }

    private synchronized void set(boolean val){
        lock.set(val);
    }

    private synchronized List<AppUser> copyList(){
        return Collections.synchronizedList(userList);
    }

    private synchronized void updateList(List<AppUser> list){
        userList = Collections.synchronizedList(list);
    }

}

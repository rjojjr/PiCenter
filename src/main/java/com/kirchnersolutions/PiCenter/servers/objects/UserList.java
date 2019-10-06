package com.kirchnersolutions.PiCenter.servers.objects;

import com.kirchnersolutions.PiCenter.entites.User;
import com.kirchnersolutions.PiCenter.entites.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class UserList {

    private List<User> userList;
    private AtomicBoolean lock;

    public UserList(){
        userList = Collections.synchronizedList(new ArrayList<>());
    }

    public synchronized boolean addUser(User user){
        while(get()){

        }
        set(true);
        boolean result = false;
        List<User> list = copyList();
        if(searchList(user.getUserName(), list) == null){
            list.add(user);
            updateList(list);
            result = true;
        }
        set(false);
        return result;
    }

    public synchronized User searchList(String userName){
        while(get()){

        }
        set(true);
        User user = searchList(userName, copyList());
        set(false);
        return user;
    }

    public synchronized boolean removeUser(String userName){
        while(get()){

        }
        set(true);
        List<User> list = copyList();
        boolean result = remove(userName, list);
        if(result){
            updateList(list);
        }
        set(false);
        return result;
    }

    private boolean remove(String userName, List<User> list){
        List<User> newList = new ArrayList<>();
        boolean result = false;
        for(User user : list){
            if(!user.getUserName().equals(userName)){
                newList.add(user);
            }else{
                result = true;
            }
        }
        if(result){
            list = newList;
        }
        return result;
    }

    private User searchList(String userName, List<User> list){
        for(User user : list){
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

    private synchronized List<User> copyList(){
        return Collections.synchronizedList(userList);
    }

    private synchronized void updateList(List<User> list){
        userList = Collections.synchronizedList(list);
    }

}

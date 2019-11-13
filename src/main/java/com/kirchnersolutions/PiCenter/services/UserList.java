package com.kirchnersolutions.PiCenter.services;

/**
 * PiCenter: Raspberry Pi home automation control center.
 * <p>
 * Copyright (C) 2019  Robert Kirchner JR
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.kirchnersolutions.PiCenter.entites.AppUser;
import com.kirchnersolutions.PiCenter.entites.UserSession;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.kirchnersolutions.PiCenter.constants.EnvironmentConstants.TEST_ACCESS_METHODS;

@Component
public class UserList {

    private List<AppUser> userList;
    private AtomicBoolean lock = new AtomicBoolean(false);

    public UserList() {
        userList = Collections.synchronizedList(new ArrayList<>());
    }

    public void dumpList() {
        if (TEST_ACCESS_METHODS) {
            updateList(Collections.synchronizedList(new ArrayList<>()));
        }
    }

    public List<AppUser> searchExpired() {
        Long time = System.currentTimeMillis();
        List<AppUser> expiredUsers = new ArrayList<>();
        for (AppUser user : copyList()) {
            UserSession userSession = user.getUserSession();
            if (userSession.getExpirationTime() <= time) {
                expiredUsers.add(user);
            }
        }
        return expiredUsers;
    }


    public synchronized boolean addUser(AppUser user) {
        while (get()) {

        }
        set(true);
        boolean result = false;
        List<AppUser> list = copyList();
        if (searchList(user.getUserName(), list) == null) {
            list.add(user);
            updateList(list);
            result = true;
        }
        set(false);
        return result;
    }

    public AppUser searchList(String userName) {
        AppUser user = searchList(userName, copyList());
        return user;
    }

    public synchronized boolean removeUser(String userName) {
        while (get()) {

        }
        set(true);
        boolean result = false;
        List<AppUser> list = copyList();
        List<AppUser> appUserList = remove(userName, list);
        if (appUserList != null) {
            updateList(appUserList);
            result = true;
        }
        set(false);
        return result;
    }

    private List<AppUser> remove(String userName, List<AppUser> list) {
        List<AppUser> newList = new ArrayList<>();
        boolean result = false;
        for (AppUser user : list) {
            if (!user.getUserName().equals(userName)) {
                newList.add(user);
            } else {
                result = true;
            }
        }
        if (result) {
            return new ArrayList<>(newList);
        }
        return null;
    }

    private static AppUser searchList(String userName, List<AppUser> list) {
        for (AppUser user : list) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    private synchronized boolean get() {
        return lock.get();
    }

    private synchronized void set(boolean val) {
        lock.set(val);
    }

    private synchronized List<AppUser> copyList() {
        return Collections.synchronizedList(userList);
    }

    private synchronized void updateList(List<AppUser> list) {
        userList = Collections.synchronizedList(list);
    }

}

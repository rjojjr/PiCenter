package com.kirchnersolutions.PiCenter.servers.beans;

public class LogonForm {

    private String username, password;

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}

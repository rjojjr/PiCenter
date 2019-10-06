package com.kirchnersolutions.PiCenter.Configuration;

import org.springframework.stereotype.Component;


@Component
public class DeviceConfiguration {

    private boolean onlyRegDevices = false, manualDevReg = false;

    public boolean isManualDevReg() {
        return manualDevReg;
    }

    public boolean isOnlyRegDevices() {
        return onlyRegDevices;
    }

    public String[] getVars(){
        String[] vars = new String[2];
        if(onlyRegDevices){
            vars[0] = "true";
        }else{
            vars[0] = "false";
        }
        if(manualDevReg){
            vars[1] = "true";
        }else{
            vars[1] = "false";
        }
        return vars;
    }

    public void setVars(String[] vars){
        if(vars[0].contains("t")){
            onlyRegDevices = true;
        }else{
            onlyRegDevices = false;
        }
        if(vars[1].contains("t")){
            manualDevReg = true;
        }else{
            manualDevReg = false;
        }
    }
}
package com.kirchnersolutions.PiCenter.services;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DeviceService {

    public DeviceService() throws Exception{
        File tokenDir = new File("PiCenter/Devices/Auth");
        File token = new File(tokenDir, "/device.key");
        File devDir = new File("PiCenter/Devices");
        File dev = new File(tokenDir, "/devices.txt");
        if(!tokenDir.exists()){
            tokenDir.mkdirs();
            token.createNewFile();
        }
        if(!devDir.exists()){
            devDir.mkdirs();
            dev.createNewFile();
        }
    }

}

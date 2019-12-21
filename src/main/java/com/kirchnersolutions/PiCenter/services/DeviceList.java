package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.Configuration.Device;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Component
public class DeviceList {

    private List<Device> deviceList;

    public DeviceList() throws FileNotFoundException, IOException {
        deviceList = Collections.synchronizedList(new ArrayList<>());
        File devDir = new File("PiCenter/Devices");
        File devices = new File(devDir, "/devices.txt");
        if(!devDir.exists()){
            devDir.mkdirs();
            devices.createNewFile();
        }else {
            Scanner in = new Scanner(devices);
            while(in.hasNextLine()){
                String line = in.nextLine();
                deviceList.add(new Device(line.split(" ")[0], line.split(" ")[1], line.split(" ")[2]));
            }
        }
    }

    public Device getDeviceByName(String name){
        for(Device device : deviceList){
            if(device.getName().equals(name)){
                return device;
            }
        }
        return null;
    }

    public List<Device> getDevices() {
        return Collections.synchronizedList(deviceList);
    }

}

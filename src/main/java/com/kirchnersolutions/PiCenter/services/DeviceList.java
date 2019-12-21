package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.Configuration.Device;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Component
public class DeviceList {

    private List<Device> deviceList;

    public DeviceList() throws FileNotFoundException {
        deviceList = Collections.synchronizedList(new ArrayList<>());
        File devices = new File("PiCenter/Devices/devices.txt");
        if(devices.exists()){
            Scanner in = new Scanner(devices);
            while(in.hasNextLine()){
                String line = in.nextLine();
                deviceList.add(new Device(line.split(" ")[0], line.split(" ")[1]));
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

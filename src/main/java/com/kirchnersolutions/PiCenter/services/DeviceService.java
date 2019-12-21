package com.kirchnersolutions.PiCenter.services;

import com.kirchnersolutions.PiCenter.Configuration.Device;
import com.kirchnersolutions.PiCenter.servers.beans.DeviceStatus;
import com.kirchnersolutions.PiCenter.servers.beans.ProcessLine;
import com.kirchnersolutions.PiCenter.servers.beans.ProcessRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class DeviceService {

    private DeviceList deviceList;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public DeviceService(DeviceList deviceList, ThreadPoolTaskExecutor threadPoolTaskExecutor) throws Exception{
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.deviceList = deviceList;
    }

    public DeviceStatus[] getDeviceStatuses(){
        return null;
    }

    private DeviceStatus getDeviceStatus(String name){
        Device device = deviceList.getDeviceByName(name);
        if(device == null){
            return null;
        }
        DeviceStatus status = new DeviceStatus();
        status.setName(name);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("token", device.getToken()); // optional - in case you auth in headers
        HttpEntity<ProcessRequest> entity = new HttpEntity<ProcessRequest>(new ProcessRequest("pitemp"), headers);
        ResponseEntity<ProcessLine> respEntity = restTemplate.exchange(device.getUrl() + "/processes", HttpMethod.PUT, entity, ProcessLine.class);
        status.setPiTempStart(respEntity.getBody().getStart());
        headers.set("token", device.getToken()); // optional - in case you auth in headers
        entity = new HttpEntity<ProcessRequest>(new ProcessRequest("dht"), headers);
        respEntity = restTemplate.exchange(device.getUrl() + "/processes", HttpMethod.PUT, entity, ProcessLine.class);
        status.setDhtStart(respEntity.getBody().getStart());
        return status;
    }

}

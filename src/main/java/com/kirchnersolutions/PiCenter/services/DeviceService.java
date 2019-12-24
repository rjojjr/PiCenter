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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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

    public DeviceStatus[] getDeviceStatuses() throws ExecutionException, InterruptedException {
        List<Device> devices = deviceList.getDevices();
        DeviceStatus[] statuses = new DeviceStatus[devices.size()];
        Future<DeviceStatus>[] futures = new Future[devices.size()];
        int count = 0;
        for(Device device : devices){
            futures[count] = threadPoolTaskExecutor.submit(() -> {
                return getDeviceStatus(device.getName());
            });
            count++;
        }
        count = 0;
        for(Future<DeviceStatus> future : futures){
            statuses[count] = future.get();
            count++;
        }
        return statuses;
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
        ResponseEntity<ProcessLine> respEntity = restTemplate.exchange("http://" + device.getUrl() + "/processes", HttpMethod.PUT, entity, ProcessLine.class);
        status.setPiTempStart(respEntity.getBody().getStart());
        headers.set("token", device.getToken()); // optional - in case you auth in headers
        entity = new HttpEntity<ProcessRequest>(new ProcessRequest("dht"), headers);
        respEntity = restTemplate.exchange(device.getUrl() + "/processes", HttpMethod.PUT, entity, ProcessLine.class);
        status.setDhtStart(respEntity.getBody().getStart());
        return status;
    }

    public DeviceStatus restartPiTemp(String name){
        Device device = deviceList.getDeviceByName(name);
        if(device == null){
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("token", device.getToken()); // optional - in case you auth in headers
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> respEntity = restTemplate.exchange("http://" + device.getUrl() + "/kill/pitemp", HttpMethod.GET, entity, String.class);
        if(!respEntity.getBody().equals("killed")){
            return null;
        }
        entity = new HttpEntity<String>("", headers);
        respEntity = restTemplate.exchange("http://" + device.getUrl() + "/start/pitemp", HttpMethod.GET, entity, String.class);
        if(!respEntity.getBody().equals("started") || !respEntity.getBody().equals("running")){
            return null;
         }
        return getDeviceStatus(name);
    }

    public DeviceStatus restartDHT(String name){
        Device device = deviceList.getDeviceByName(name);
        if(device == null){
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("token", device.getToken()); // optional - in case you auth in headers
        HttpEntity<String> entity = new HttpEntity<String>("", headers);
        ResponseEntity<String> respEntity = restTemplate.exchange("http://" + device.getUrl() + "/kill/dht", HttpMethod.GET, entity, String.class);
        if(!respEntity.getBody().equals("killed")){
            return null;
        }
        entity = new HttpEntity<String>("", headers);
        respEntity = restTemplate.exchange("http://" + device.getUrl() + "/start/dht", HttpMethod.GET, entity, String.class);
        if(!respEntity.getBody().equals("started") || !respEntity.getBody().equals("running")){
            return null;
        }
        return getDeviceStatus(name);
    }

}

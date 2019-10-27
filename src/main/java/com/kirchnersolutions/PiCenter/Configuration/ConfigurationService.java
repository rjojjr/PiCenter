package com.kirchnersolutions.PiCenter.Configuration;
/**
 *2019 Kirchner Solutions
 * @Author Robert Kirchner Jr.
 *
 * This code may not be decompiled, recompiled, copied, redistributed or modified
 * in any way unless given express written consent from Kirchner Solutions.
 */

import com.kirchnersolutions.PiCenter.dev.DevelopmentException;
import com.kirchnersolutions.PiCenter.servers.objects.serial.ConfigSerializer;
import com.kirchnersolutions.utilities.ByteTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

@DependsOn({"devVars", "sysVars", "tableConfiguration", "socketServerConfiguration", "deviceConfiguration"})
@Service
public class ConfigurationService {

    public static final boolean DISTRIBUTION_COPY = false;

    @Autowired
    private DevVars devVars;
    @Autowired
    private SysVars sysVars;
    @Autowired
    private TableConfiguration tableConfiguration;
    @Autowired
    private SocketServerConfiguration socketServerConfiguration;
    @Autowired
    private DeviceConfiguration deviceConfiguration;

    private ConfigSerializer configSerializer = new ConfigSerializer();
    private File configDir, devConfig, sysConfig, tableConfig, socketConfig, deviceConfig;

    public ConfigurationService() throws Exception{

    }

    @PostConstruct
    public void init() throws Exception{
        File temp = new File("SHome");
        configDir = new File(temp, "/Configuration/Files");
        devConfig = new File(configDir, "/DeveloperConfig.dbs");
        sysConfig = new File(configDir, "/SystemConfig.dbs");
        tableConfig = new File(configDir, "/TableConfig.dbs");
        socketConfig = new File(configDir, "/SocketServerConfig.dbs");
        deviceConfig = new File(configDir, "/DeviceConfig.dbs");
        if(DISTRIBUTION_COPY){

        }
        if(!configDir.exists()){
            configDir.mkdirs();
            try{
                devConfig.createNewFile();
                ByteTools.writeBytesToFile(devConfig, "true%true%true%true%true%true".getBytes("UTF-8"));
            }catch (Exception e){
                System.out.println("Failed to initialize DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                throw new DevelopmentException("Failed to initialize DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
            }
            try{
                sysConfig.createNewFile();
                ByteTools.writeBytesToFile(sysConfig, "UTF-8%win%none".getBytes("UTF-8"));
            }catch (Exception e){
                System.out.println("Failed to initialize SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                throw new DevelopmentException("Failed to initialize SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
            }
            try{
                tableConfig.createNewFile();
                ByteTools.writeBytesToFile(tableConfig, "20%100000%500%2".getBytes("UTF-8"));
            }catch (Exception e){
                System.out.println("Failed to initialize TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                throw new DevelopmentException("Failed to initialize TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
            }
            try{
                socketConfig.createNewFile();
                ByteTools.writeBytesToFile(socketConfig, "7773%3333%3334".getBytes("UTF-8"));
            }catch (Exception e){
                System.out.println("Failed to initialize SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                throw new DevelopmentException("Failed to initialize SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
            }
            try{
                deviceConfig.createNewFile();
                ByteTools.writeBytesToFile(deviceConfig, "false%false".getBytes("UTF-8"));
            }catch (Exception e){
                System.out.println("Failed to initialize DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                throw new DevelopmentException("Failed to initialize DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
            }
        }else{
            if(!devConfig.exists()){
                try{
                    devConfig.createNewFile();
                    ByteTools.writeBytesToFile(devConfig, "true%true%true%true%true%true".getBytes(sysVars.getCharacterEncoding()));
                }catch (Exception e){
                    System.out.println("Failed to initialize DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to initialize DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }else{
                try{
                    String[] vars = configSerializer.deserialize(ByteTools.readBytesFromFile(devConfig));
                    System.out.println("Setting vars");
                    devVars.setVars(vars);
                }catch (Exception e){
                    System.out.println("Failed to read DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to read DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }
            if(!sysConfig.exists()){
                try{
                    sysConfig.createNewFile();
                    ByteTools.writeBytesToFile(sysConfig, "UTF-8%win%none".getBytes("UTF-8"));
                }catch (Exception e){
                    System.out.println("Failed to initialize SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to initialize SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }else{
                try{
                    String[] vars = configSerializer.deserialize(ByteTools.readBytesFromFile(sysConfig));
                    sysVars.setVars(vars);
                }catch (Exception e){
                    System.out.println("Failed to read SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to read SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }
            if(!tableConfig.exists()){
                try{
                    tableConfig.createNewFile();
                    ByteTools.writeBytesToFile(tableConfig, "20%100000%500%2".getBytes("UTF-8"));
                }catch (Exception e){
                    System.out.println("Failed to initialize TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to initialize TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }else{
                try{
                    String[] vars = configSerializer.deserialize(ByteTools.readBytesFromFile(tableConfig));
                    tableConfiguration.setVars(vars);
                }catch (Exception e){
                    System.out.println("Failed to read TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to read TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }
            if(!socketConfig.exists()){
                try{
                    socketConfig.createNewFile();
                    ByteTools.writeBytesToFile(socketConfig, "4444%3333%3334".getBytes("UTF-8"));
                }catch (Exception e){
                    System.out.println("Failed to initialize SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to initialize SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }else{
                try{
                    String[] vars = configSerializer.deserialize(ByteTools.readBytesFromFile(socketConfig));
                    socketServerConfiguration.setVars(vars);
                }catch (Exception e){
                    System.out.println("Failed to read SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to read SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }
            if(!deviceConfig.exists()){
                try{
                    deviceConfig.createNewFile();
                    ByteTools.writeBytesToFile(deviceConfig, "false%false".getBytes("UTF-8"));
                }catch (Exception e){
                    System.out.println("Failed to initialize DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to initialize DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }else{
                try{
                    String[] vars = configSerializer.deserialize(ByteTools.readBytesFromFile(deviceConfig));
                    deviceConfiguration.setVars(vars);
                }catch (Exception e){
                    System.out.println("Failed to read DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to read DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                }
            }
        }
    }

    /**
     * Updates the variables of configuration type with newValues.
     * @param type
     * @param newValues
     * @throws DevelopmentException
     * @throws Exception
     */
    public void updateConfig(String type, String[] newValues) throws DevelopmentException, Exception{
        UpdateConfig(type, newValues);
    }

    public String[] getVarsNames(String type){
        return GetVarsNames(type);
    }


    private void UpdateConfig(String type, String[] newValues) throws DevelopmentException, Exception{
        if(type.equals("dev")){
            devVars.setVars(newValues);
            try{
                ByteTools.writeBytesToFile(devConfig, configSerializer.serialize(devVars.getVars()));
            }catch (Exception e){
                if(devVars.isDevExceptions()){
                    System.out.println("Failed to write to DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to write to DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService updateConfig");
                }else {
                    System.out.println("Failed to write to DeveloperConfig.dbs: " + e.getMessage());
                    throw new Exception("Failed to write to DeveloperConfig.dbs: " + e.getMessage() + " Class ConfigurationService updateConfig");
                }
            }
        }else if(type.equals("sys")){
            sysVars.setVars(newValues);
            try{
                ByteTools.writeBytesToFile(sysConfig, configSerializer.serialize(sysVars.getVars()));
            }catch (Exception e){
                if(devVars.isDevExceptions()){
                    System.out.println("Failed to write to SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to write to SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method updateConfig");
                }else {
                    System.out.println("Failed to write to SystemConfig.dbs: " + e.getMessage());
                    throw new Exception("Failed to write to SystemConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method updateConfig");
                }
            }
        }else if(type.equals("table")){
            tableConfiguration.setVars(newValues);
            try{
                ByteTools.writeBytesToFile(tableConfig, configSerializer.serialize(tableConfiguration.getVars()));
            }catch (Exception e){
                if(devVars.isDevExceptions()){
                    System.out.println("Failed to write to TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to write to TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method updateConfig");
                }else {
                    System.out.println("Failed to write to SystemConfig.dbs: " + e.getMessage());
                    throw new Exception("Failed to write to TableConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method updateConfig");
                }
            }
        }else if(type.equals("socket")){
            socketServerConfiguration.setVars(newValues);
            try{
                ByteTools.writeBytesToFile(socketConfig, configSerializer.serialize(socketServerConfiguration.getVars()));
            }catch (Exception e){
                if(devVars.isDevExceptions()){
                    System.out.println("Failed to write to SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to write to SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method updateConfig");
                }else {
                    System.out.println("Failed to write to SocketServerConfig.dbs: " + e.getMessage());
                    throw new Exception("Failed to write to SocketServerConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method updateConfig");
                }
            }
        }else if(type.equals("device")){
            deviceConfiguration.setVars(newValues);
            try{
                ByteTools.writeBytesToFile(deviceConfig, configSerializer.serialize(deviceConfiguration.getVars()));
            }catch (Exception e){
                if(devVars.isDevExceptions()){
                    System.out.println("Failed to write to DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method Constructor");
                    throw new DevelopmentException("Failed to write to DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method updateConfig");
                }else {
                    System.out.println("Failed to write to DeviceConfig.dbs: " + e.getMessage());
                    throw new Exception("Failed to write to DeviceConfig.dbs: " + e.getMessage() + " Class ConfigurationService Method updateConfig");
                }
            }
        }
    }

    private String[] GetVars(String type){
        if(type.equals("dev")){
            return devVars.getVars();
        }
        if(type.equals("sys")){
            return sysVars.getVars();
        }
        if(type.equals("table")){
            return tableConfiguration.getVars();
        }
        if(type.equals("socket")){
            return socketServerConfiguration.getVars();
        }
        if(type.equals("device")){
            return deviceConfiguration.getVars();
        }
        return null;
    }

    private String[] GetVarsNames(String type){
        if(type.equals("dev")){
            return devVars.getVarsNames();
        }
        if(type.equals("sys")){
            return sysVars.getVarsNames();
        }
        if(type.equals("table")){
            return tableConfiguration.getVarsNames();
        }
        if(type.equals("socket")){
            return socketServerConfiguration.getVarsNames();
        }
        if(type.equals("device")){
            return deviceConfiguration.getVars();
        }
        return null;
    }

    String[] getDevVars(){
        return devVars.getVars();
    }

}
package com.kirchnersolutions.PiCenter.Configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
public class SocketServerConfiguration {

    private int port = 4444, remoteSupportPort = 3333, terminalInterfacePort = 3334;

    public void setVars(String[] vars){
        try{
            port = Integer.parseInt(vars[0]);
            remoteSupportPort = Integer.parseInt(vars[1]);
            terminalInterfacePort = Integer.parseInt(vars[2]);
        }catch (Exception e){
            System.out.println("Failed to parse socket server ports...");
            System.out.println("Defaulting to port 4444");
            System.out.println("Defaulting to remote support port 4333");
            System.out.println("Defaulting to terminal interface port 4344");
            port = 4444;
            remoteSupportPort = 3333;
            terminalInterfacePort = 3334;
        }
    }

    public String[] getVars(){
        String[] vars = new String[3];
        vars[0] = "" + port;
        vars[1] = "" + remoteSupportPort;
        vars[2] = "" + terminalInterfacePort;
        return vars;
    }

    public String[] getVarsNames(){
        String[] vars = new String[3];
        vars[0] = "Application Driver Port:" + port;
        vars[1] = "Remote Support Port:" + remoteSupportPort;
        vars[2] = "Terminal Interface Port:" + remoteSupportPort;
        return vars;
    }
}
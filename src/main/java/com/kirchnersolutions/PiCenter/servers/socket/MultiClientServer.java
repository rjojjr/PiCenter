/*
 * kData Performance Database
 *
 * Version: 1.0.00b
 *
 * Robert Kirchner Jr.
 * 2018 Kirchner Solutions
 *
 * This code is not to be distributed, compiled, decompiled
 * copied, used, recycled, moved or modified in any way without
 * express written permission from Kirchner Solutions
 */
package com.kirchnersolutions.PiCenter.servers.socket;


import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.servers.objects.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MultiClientServer Version 1.0.00b
 *
 * @author Robert Kirchner Jr. 2018 Kirchner Solutions
 */
@DependsOn({"threadPoolTaskExecutor", "debuggingService", "transactionService", "sessionService"})
@Component
public class MultiClientServer {

    @Autowired
    private volatile ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private DebuggingService debuggingService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ObjectFactory databaseObjectFactory;

    private volatile int port = 0;
    private volatile static AtomicBoolean running = new AtomicBoolean(false);
    private volatile static SocketServer server;

    private static volatile MultiClientServer single_instance = null;

    public MultiClientServer() throws Exception {

    }

    @PostConstruct
    public void init() throws Exception{
        System.out.println("Socket Server created");
        this.port = debuggingService.getSocketPort();
        server = new SocketServer(new ServerSocket(), threadPoolTaskExecutor, debuggingService, databaseObjectFactory, transactionService, port, running);
        start();
    }


    public synchronized boolean isRunning() {
        return server.isRunning();
    }

    void manualStart() throws Exception {
        if (!isRunning()) {
            threadPoolTaskExecutor.execute(server);
        }
    }

    public boolean start() throws Exception {
        if (!isRunning()) {
            ///threadPoolTaskExecutor.execute(server);
            try {
                threadPoolTaskExecutor.execute(server);
                running.set(true);
                //threadPoolTaskExecutor.execute(new SocketServer(new ServerSocket(), threadPoolTaskExecutor, transactionService, transactionSerializer,
                //sessionService, debuggingService, databaseObjectFactory, port, running));
                return true;
            } catch (Exception e) {
                running.set(false);
                debuggingService.socketDebug(e.getMessage());
                debuggingService.nonFatalDebug(debuggingService.getStack(e));
                return false;
            }
        }
        return false;
    }


    public String getStats() {
        try{
            if(isRunning()){
                return "true," + getPort();
            }else {
                // multiClientServer.manualStart();
                return "false";

            }
        }catch (Exception e){
            return e.getMessage();
        }
    }

    public boolean stop() throws Exception {
        if (isRunning()) {
            server.stop();
            return true;
        }
        return false;
    }

    int getPort() {
        return port;
    }


}


package com.kirchnersolutions.PiCenter.servers.socket;


import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.dev.DevelopmentException;
import com.kirchnersolutions.PiCenter.servers.objects.ObjectFactory;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
class SocketServer implements Runnable{

    private volatile ServerSocket serverSocket;
    private volatile ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private DebuggingService debuggingService;
    private ObjectFactory objectFactory;
    private TransactionService transactionService;

    private int port = 0;
    private volatile AtomicBoolean running = new AtomicBoolean(false);

    /*
    public SocketServer(ThreadPoolTaskExecutor threadPoolTaskExecutor, TransactionService transactionService, TransactionSerializer transactionSerializer, SessionService sessionService, DebuggingService debuggingService){
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

     */

    void setPort(int nPort){
        port = nPort;
    }

    public void run(){
        Thread.currentThread().setName("SocketServer port " + port);
        try{
            running.set(true);
            startServer(port);

        }catch (Exception e){
            e.printStackTrace();
            running.set(false);
            debuggingService.socketDebug("Failed to start socket server on port " + port);
            //debuggingService.throwDevException(new DevelopmentException("Failed to start socket server on port " + port + " " + e.getMessage()));
            debuggingService.nonFatalDebug("Failed to start socket server on port " + port + " " + e.getMessage());
        }
        debuggingService.socketDebug("Failed to start socket server on port " + port);
        debuggingService.nonFatalDebug("Failed to start socket server on port " + port);
        running.set(false);
    }

    synchronized boolean isRunning(){
        return running.get();
    }

    synchronized void setRunning(boolean run){
        running.set(run);
    }

    public void manualStart() throws Exception{
        try {
            setRunning(true);
            serverSocket = new ServerSocket();
            InetAddress inetAddress = InetAddress.getLocalHost();
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName(inetAddress.getHostAddress()), port));
            debuggingService.socketDebug("Socket server manually started on port " + port);
            while (true) {
                threadPoolTaskExecutor.execute(new IndependentClientHandler(serverSocket.accept(), debuggingService, this));
            }
        } catch (Exception ex) {
            setRunning(false);
            debuggingService.socketDebug("Failed to manually start socket server on port " + port + ex.getMessage());
            debuggingService.throwDevException(new DevelopmentException("Failed to manually start socket server on port " + port + " " + ex.getMessage()));
            debuggingService.nonFatalDebug("Failed to manually start socket server on port " + port + " " + ex.getMessage());
            Logger.getLogger(MultiClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void startServer(int port) throws Exception{
        try {
            debuggingService.socketDebug("Attempting to start socket server on port " + port);
            setRunning(true);
            serverSocket = new ServerSocket();
            InetAddress inetAddress = InetAddress.getLocalHost();
            serverSocket.bind(new InetSocketAddress(InetAddress.getByName(inetAddress.getHostAddress()), port));
        //serverSocket = new ServerSocket(port, 1, InetAddress.getByName(inetAddress.getHostAddress()));
            debuggingService.socketDebug("Socket server started on port " + port);
            while (true) {
                threadPoolTaskExecutor.execute(new IndependentClientHandler(serverSocket.accept(), debuggingService, this));
            }
            //debuggingService.socketDebug("Socket server started on port " + port);
        } catch (Exception ex) {
            setRunning(false);
            throw ex;
            //setRunning(false);
            //debuggingService.socketDebug("Failed to start socket server on port " + port + ex.getMessage());
            //debuggingService.throwDevException(new DevelopmentException("Failed to start socket server on port " + port + " " + ex.getMessage()));
            //debuggingService.nonFatalDebug("Failed to start socket server on port " + port + " " + ex.getMessage());
            //Logger.getLogger(MultiClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() throws Exception{
        try {
            serverSocket.close();
            setRunning(false);
            debuggingService.socketDebug("Socket server stopped on port " + port);
        } catch (Exception ex) {
            setRunning(false);
            ex.printStackTrace();
            debuggingService.socketDebug("Failed to stop socket server on port " + port);
            debuggingService.throwDevException(new DevelopmentException("Failed to stop socket server on port " + port + " " + ex.getMessage()));
            debuggingService.nonFatalDebug("Failed to stop socket server on port " + port + " " + ex.getMessage());
            Logger.getLogger(MultiClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    byte[] processInput(byte[] input) throws Exception{
        try{
            return null;
            //return transactionService.submitTransaction((Transaction) objectFactory.databaseObjectFactory(Base64.getDecoder().decode(input)), session);
        }catch (Exception e){
            debuggingService.socketDebug("Failed to parse socket input on port " + port);
            debuggingService.throwDevException(new DevelopmentException("Failed to parse socket input on port " + port + " " + e.getStackTrace().toString()));
            debuggingService.nonFatalDebug("Failed to parse socket input on port " + port + " " + e.getStackTrace().toString());
        }
        return Base64.getEncoder().encode("Failed".getBytes("UTF-8"));
    }

    /*
    void invalidate(Session session, int port) throws Exception{
        try{
            if(session.getUser() != null){
                sessionService.logOff(4, session);
            }
        }catch (Exception e){
            debuggingService.socketDebug("Failed to invalidate socket session on port " + port);
            debuggingService.throwDevException(new DevelopmentException("Failed to invalidate socket session on port " + port + " " + e.getStackTrace().toString()));
            debuggingService.nonFatalDebug("Failed to invalidate socket session on port " + port + " " + e.getStackTrace().toString());
        }
    }

     */
}
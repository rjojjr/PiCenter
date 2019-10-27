/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirchnersolutions.PiCenter.servers.socket;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;

import java.util.Base64;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rjojj
 */
class IndependentClientHandler implements Runnable {

    private Socket clientSocket;
    private BufferedWriter out;
    private BufferedReader in;

    private DebuggingService debuggingService;

    private TransactionService transactionService;

    private SocketServer socketServer;

    private volatile boolean loggedOn = false;
    private String userName = "";

    private boolean stop;

    public IndependentClientHandler(Socket socket, DebuggingService debuggingService, SocketServer socketServer, TransactionService transactionService) {
        this.clientSocket = socket;
        this.socketServer = socketServer;
        this.debuggingService = debuggingService;
        this.transactionService = transactionService;
        stop = false;
    }

    public String getUserName() {
        synchronized (this) {
            return new String(userName);
        }
    }

    public void stopThread() {
        synchronized (this) {
            this.stop = true;
        }
    }

    @Override
    public void run() {
        Keys keys = new Keys();
        String ip = clientSocket.getRemoteSocketAddress().toString().split("/")[1];
        int port = clientSocket.getPort();
        Thread.currentThread().setName("SocketSession port " + port + " address " + ip);
        debuggingService.trace("Started socket client thread: " + Thread.currentThread().getName());
        debuggingService.socketDebug("Socket client connected on port " + port + " address " + ip);
        //transMan = TransactionManager.getInstance();
        try {
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException ex) {
            debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " failed to init output stream");
            debuggingService.socketDebug("Failure to open client socket output on port " + port + " " + ex.getMessage());
            debuggingService.nonFatalDebug("Failure to open client socket output on port " + port + " " + ex.getMessage());
            Logger.getLogger(MultiClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " failed to init input stream");
            debuggingService.socketDebug("Failure to open client socket input on port " + port + " " + ex.getMessage());
            debuggingService.nonFatalDebug("Failure to open client socket input on port " + port + " " + ex.getMessage());
            Logger.getLogger(MultiClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        String inputLine;
        boolean key = false;
        try {
            while ((inputLine = in.readLine()) != null) {
                debuggingService.trace("Socket client on port " + port + " input received = " + inputLine);
                if(!key){
                    debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " Init handshake");
                    if(keys.getPublicKey(Base64.getDecoder().decode(inputLine))){
                        key = true;
                        byte[] keyOut;
                        if((keyOut = keys.encryptAESKey()).length == 0){
                            debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " failed to init AES key");
                            debuggingService.socketDebug("Failed to get generate AES key for client " + ip + " on port " + port + "\r\nConnection closed.");
                            debuggingService.nonFatalDebug("Failed to get generate AES key for client " + ip + " on port " + port + "\r\nConnection closed.");
                            break;
                        }
                        debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " sent AES key");
                        debuggingService.socketDebug("Sent AES key to client " + ip + " on port " + port);
                        out.write(Base64.getEncoder().encodeToString(keyOut) + "\n");
                        out.flush();
                    }else{
                        debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " Failed to get public key from client ");
                        debuggingService.socketDebug("Failed to get public key from client " + ip + " on port " + port + "\r\nConnection closed.");
                        debuggingService.nonFatalDebug("Failed to get public key from client " + ip + " on port " + port + "\r\nConnection closed.");
                        break;
                    }
                }else {
                    debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " received client input");
                    String output = Base64.getEncoder().encodeToString(keys.encryptAESRequest(transactionService.inputRequest((keys.decryptAESResponse(inputLine)))));
                    System.out.println(output);
                    out.flush();
                    debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " received response to client");
                }
            }
            in.close();
            out.close();
            debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " closed streams");
        } catch (IOException ex) {
            try {
            } catch (Exception f) {
            }
            debuggingService.socketDebug("Failure to read client socket input on port " + port + " " + DebuggingService.getStackTrace(ex));
            debuggingService.nonFatalDebug("Failure to read client socket input on port " + port + " " + DebuggingService.getStackTrace(ex));
            Logger.getLogger(MultiClientServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception es) {
            try {
            } catch (Exception f) {

            }
            es.printStackTrace();
        }

        try {
            clientSocket.close();
            debuggingService.trace("Socket client thread: " + Thread.currentThread().getName() + " closed client socket");
            debuggingService.socketDebug("Closed client socket on port " + port);
        } catch (Exception ex) {
            try {
            } catch (Exception f) {
            }
            debuggingService.socketDebug("Failure to close client socket input on port " + port + " " + ex.getMessage());
            debuggingService.nonFatalDebug("Failure to close client socket input on port " + port + " " + ex.getMessage());
            Logger.getLogger(MultiClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

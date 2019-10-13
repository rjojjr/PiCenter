package com.kirchnersolutions.PiCenter.servers.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackupClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private volatile AtomicBoolean connected = new AtomicBoolean(false);

    private String host, add;
    private int p;

    public boolean startConnection(String ip, int port) throws IOException, IllegalArgumentException {
        this.add = ip;
        this.p = port;
        if(ip.equals("")){
            throw new IllegalArgumentException("Invalid Backup Address");
        }
        if(port <= 0){
            throw new IllegalArgumentException("Invalid host port");
        }
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        connected.set(true);
        return true;
    }

    public boolean isConnected(){
        return connected.get();
    }

    public String sendMessage(String msg) throws IOException{
        try{
            if(isConnected()){

                out.println(new String(Base64.getEncoder().encode(msg.getBytes("UTF-8")), "UTF-8"));
                // System.out.println("here");
                //System.out.println("null");
                String resp = in.readLine();
                resp = new String(Base64.getDecoder().decode(resp.getBytes("UTF-8")), "UTF-8");
                if(resp.contains("-close")){
                    stopConnection();
                    connected.set(false);

                    return "closed";
                }
                //System.out.println("here");
                //System.out.println(resp);
                return resp;
            }
        }catch (Exception e){
            e.printStackTrace();
            connected.set(false);
        }
        //System.out.println("here");
        stopConnection();
        return null;
    }

    public void stopConnection() throws IOException {
        connected.set(false);
        in.close();
        out.close();
        clientSocket.close();
    }



}

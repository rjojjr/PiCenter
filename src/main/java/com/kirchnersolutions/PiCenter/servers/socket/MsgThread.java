package com.kirchnersolutions.PiCenter.servers.socket;

import java.util.concurrent.Callable;

public class MsgThread implements Callable<String> {

    private String msg;
    private BackupClient backupClient;

    public MsgThread(String msg, BackupClient backupClient) {
        this.msg = msg;
        this.backupClient = backupClient;
    }

    @Override
    public String call() throws Exception {
        if (backupClient.isConnected()) {
            //System.out.println("here");
            String out = backupClient.sendMessage(msg);
            if(out.contains("f")){
                return "null";
            }
            ///System.out.println(out);
            return out;
        }
        return "null";
    }
}
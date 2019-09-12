package com.kirchnersolutions.PiCenter;


import kirchnersolutions.javabyte.driver.common.driver.DatabaseResults;
import kirchnersolutions.javabyte.driver.common.driver.Transaction;
import kirchnersolutions.javabyte.driver.singleclient.SingleClient;

import java.math.BigInteger;
import java.util.*;

class Users {

    private List<SingleClient> clients;
    private static Users instance = new Users();

    private Users(){
        clients = Collections.synchronizedList(new ArrayList<>());
    }

    public static Users getInstance(){
        return instance;
    }

    synchronized boolean logon(String username, String password) throws Exception{
        SingleClient client = new SingleClient("192.168.1.25", " ", 4444, username, password);
        if(client.logon()){
            List<SingleClient> temp = getClients();
            temp.add(client);
            updateClients(temp);
            return true;
        }
        return false;
    }

    synchronized boolean logout(String username) throws Exception{
        List<SingleClient> temp = getClients();
        int count = 0;
        for(SingleClient client : temp){
            if(client != null){
                if(client.getUsername().equals(username)){
                    client.logout();
                    temp.set(count, null);
                    trimList(temp);
                    return true;
                }
            }
            count++;
        }
        return false;
    }

    synchronized SingleClient getClient(String username){
        List<SingleClient> temp = getClients();
        for(SingleClient client : temp){
            if(client != null){
                if(client.getUsername().equals(username)){
                    return client;
                }
            }
        }
        return null;
    }

    synchronized List<List<Wrapper>> getTempList(String username) throws Exception{
        List<List<Wrapper>> out = new ArrayList<>();
        SingleClient client = getClient(username);
        if(client != null){
            Map<String, String> where = new HashMap<>();
            Transaction transaction = new Transaction();
            transaction.setUsername(username);
            transaction.setOperation("SELECT ADVANCED PiTempsOffice");
            transaction.setWhere(where);
            //transaction.setHowMany(new BigInteger("720"));
            transaction.setHowMany(new BigInteger("-1"));
            DatabaseResults office = client.sendCommand(transaction);
            out.add(processResult(office));
            where = new HashMap<>();
            transaction = new Transaction();
            transaction.setUsername(username);
            transaction.setOperation("SELECT ADVANCED PiTempsServerRoom");
            transaction.setWhere(where);
            //transaction.setHowMany(new BigInteger("720"));
            transaction.setHowMany(new BigInteger("-1"));
            DatabaseResults server = client.sendCommand(transaction);
            out.add(processResult(server));
            where = new HashMap<>();
            transaction = new Transaction();
            transaction.setUsername(username);
            transaction.setOperation("SELECT ADVANCED PiTempsLR");
            transaction.setWhere(where);
            //transaction.setHowMany(new BigInteger("720"));
            transaction.setHowMany(new BigInteger("-1"));
            DatabaseResults lr = client.sendCommand(transaction);
            out.add(processResult(lr));
            where = new HashMap<>();
            transaction = new Transaction();
            transaction.setUsername(username);
            transaction.setOperation("SELECT ADVANCED PiTempsBedroom");
            transaction.setWhere(where);
            //transaction.setHowMany(new BigInteger("720"));
            transaction.setHowMany(new BigInteger("-1"));
            DatabaseResults br = client.sendCommand(transaction);
            out.add(processResult(br));
            return out;
        }
        return null;
    }

    private synchronized void trimList(){
        List<SingleClient> temp = getClients();
        List<SingleClient> temp1 = Collections.synchronizedList(new ArrayList<>());
        for(SingleClient client : temp){
            if(client != null){
                temp1.add(client);
            }
        }
        updateClients(temp1);
    }

    private synchronized void trimList(List<SingleClient> temp){
        List<SingleClient> temp1 = Collections.synchronizedList(new ArrayList<>());
        for(SingleClient client : temp){
            if(client != null){
                temp1.add(client);
            }
        }
        updateClients(temp1);
    }

    private synchronized List<SingleClient> getClients(){
        return Collections.synchronizedList(new ArrayList<>(clients));
    }

    private synchronized void updateClients(List<SingleClient> newList){
        clients = Collections.synchronizedList(new ArrayList<>(newList));
    }

    private static List<Wrapper> processResult(DatabaseResults results){
        List<Wrapper> out = new ArrayList<>();
        int count = 0;
        int[][] avg = new int[720][2];
        for(Map<String, String> map : results.getResults()){
            Map<String, String> map1 = (HashMap<String, String>)map;
            if(count == 0){
                avg[0][0] = Integer.parseInt(map1.get("temp"));
                avg[0][1] = Integer.parseInt(map1.get("humidity"));
                out.add(new Wrapper("Right Now: Temp: " + map1.get("temp") + "F Humidity: " + map1.get("humidity") + "%"));
            }else if(count == 29){
                avg[count][0] = Integer.parseInt(map1.get("temp"));
                avg[count][1] = Integer.parseInt(map1.get("humidity"));
                int t = 0, h = 0;
                for(int i = 0; i < 30; i++){
                    t+= avg[i][0];
                    h+= avg[i][1];
                }
                t/= 30;
                h/= 30;
                out.add(new Wrapper("Average of Last Hour: Temp: " + t + "F Humidity: " + h + "%"));
            }else if(count == 179){
                avg[count][0] = Integer.parseInt(map1.get("temp"));
                avg[count][1] = Integer.parseInt(map1.get("humidity"));
                int t = 0, h = 0;
                for(int i = 0; i <= 179; i++){
                    t+= avg[i][0];
                    h+= avg[i][1];
                }
                t/= 180;
                h/= 180;
                out.add(new Wrapper("Average of Last 6 Hours: Temp: " + t + "F Humidity: " + h + "%"));
            }else if(count == 359){
                avg[count][0] = Integer.parseInt(map1.get("temp"));
                avg[count][1] = Integer.parseInt(map1.get("humidity"));
                int t = 0, h = 0;
                for(int i = 0; i < 360; i++){
                    t+= avg[i][0];
                    h+= avg[i][1];
                }
                t/= 360;
                h/= 360;
                out.add(new Wrapper("Average of Last 12 Hours: Temp: " + t + "F Humidity: " + h + "%"));
            }else if(count == 719){
                avg[count][0] = Integer.parseInt(map1.get("temp"));
                avg[count][1] = Integer.parseInt(map1.get("humidity"));
                int t = 0, h = 0;
                for(int i = 0; i < 720; i++){
                    t+= avg[i][0];
                    h+= avg[i][1];
                }
                t/= 720;
                h/= 720;
                out.add(new Wrapper("Average of Last 24 Hours: Temp: " + t + "F Humidity: " + h + "%"));
                break;
            }else{
                avg[count][0] = Integer.parseInt(map1.get("temp"));
                avg[count][1] = Integer.parseInt(map1.get("humidity"));
            }
            count++;
        }
        return out;
    }

}

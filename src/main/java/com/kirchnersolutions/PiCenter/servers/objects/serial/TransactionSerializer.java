package com.kirchnersolutions.PiCenter.servers.objects.serial;
/**
 *2019 Kirchner Solutions
 * @Author Robert Kirchner Jr.
 *
 * This code may not be decompiled, recompiled, copied, redistributed or modified
 * in any way unless given express written consent from Kirchner Solutions.
 */

import com.kirchnersolutions.PiCenter.dev.exceptions.SerialException;
import com.kirchnersolutions.PiCenter.dev.exceptions.TransactionSerialException;
import com.kirchnersolutions.PiCenter.servers.objects.Transaction;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class TransactionSerializer implements Serializer<Transaction> {

    /**
     *Serialize Transaction object into byte[].
     * @param transaction
     * @Overide
     * @return
     * @throws Exception
     */
    @Override
    public byte[] serialize(Transaction transaction) throws Exception{
        byte[] finalBytes, stringBytes, selectBytes, oldvaluesBytes, resultsBytes, putBytes, put2Bytes, whereBytes, where2Bytes, newrowsBytes, newRow2Bytes;
        String string = new String();
        if (transaction.isSuccessfull()) {
            string = ("true");
        } else {
            string = ("false");
        }
        char breakChar = '~';
        string+= (breakChar);
        string+= (transaction.getUsername());

        string+= (breakChar);
        string+= (transaction.getOperation());
        string+= (breakChar);
        string+= (transaction.getRequestTime());
        string+= (breakChar);
        string+= (transaction.getFinishTime());
        string+= (breakChar);
        string+= (transaction.getHowMany());
        if(transaction.getFailMessage().equals("")){
            string+= (breakChar);
            string+= (" ");
        }else{
            string+= (breakChar);
            string+= (transaction.getFailMessage());
        }
        string+= (breakChar);
        string+= (transaction.getUserIndex());
        string+= (breakChar);
        string+= (transaction.getTransactionID());
        string+= (breakChar);
        string+= (transaction.getPassword());
        stringBytes = string.getBytes("UTF-8");
        if (transaction.getPut() != null) {
            try {
                putBytes = (serializeObject(transaction.getPut()));
            } catch (Exception e) {
                throw new Exception("Failed to serialize put map");
            }
        } else {
            putBytes = new byte[1];
            putBytes[0] = " ".getBytes("UTF-8")[0];
        }

        if (transaction.getWhere() != null) {
            try {
                whereBytes = (serializeObject(transaction.getWhere()));
            } catch (Exception e) {
                throw new Exception("Failed to serialize where map");
            }
        } else {
            whereBytes = new byte[1];
            whereBytes[0] = " ".getBytes("UTF-8")[0];
        }

        if (transaction.getResults() != null) {
            try {
                resultsBytes = (serializeObject(transaction.getResults()));
            } catch (Exception e) {
                throw new Exception("Failed to serialize results map");
            }
        } else {
            resultsBytes = new byte[1];
            resultsBytes[0] = " ".getBytes("UTF-8")[0];
        }
        if (transaction.getOldValues() != null) {
            try {
                oldvaluesBytes = (serializeObject(transaction.getOldValues()));
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Failed to serialize rollback map");
            }
        } else {
            oldvaluesBytes = new byte[1];
            oldvaluesBytes[0] = " ".getBytes("UTF-8")[0];
        }
        if (transaction.getSelect() != null) {
            try {
                selectBytes = (serializeObject(transaction.getSelect()));
            } catch (Exception e) {
                throw new Exception("Failed to serialize select map");
            }
        } else {
            selectBytes = new byte[1];
            selectBytes[0] = " ".getBytes("UTF-8")[0];
        }
        if (transaction.getPut2() != null) {
            try {
                put2Bytes = (serializeObject(transaction.getPut2()));
            } catch (Exception e) {
                throw new Exception("Failed to serialize put2 map");
            }
        } else {
            put2Bytes = new byte[1];
            put2Bytes[0] = " ".getBytes("UTF-8")[0];
        }
        if (transaction.getWhere2() != null) {
            try {
                where2Bytes = (serializeObject(transaction.getWhere2()));
            } catch (Exception e) {
                throw new TransactionSerialException("Failed to serialize where2 map");
            }
        } else {
            where2Bytes = new byte[1];
            where2Bytes[0] = " ".getBytes("UTF-8")[0];
        }
        string+= (breakChar);
        string+= ((transaction.getFailMessage()));
        if (transaction.getNewRows() != null) {
            try {
                newrowsBytes = (serializeObject(transaction.getNewRows()));
            } catch (Exception e) {
                throw new TransactionSerialException("Failed to serialize newRows list");
            }
        } else {
            newrowsBytes = new byte[1];
            newrowsBytes[0] = " ".getBytes("UTF-8")[0];
        }
        if (transaction.getNewRows2() != null) {
            try {
                newRow2Bytes = (serializeObject(transaction.getNewRows2()));
            } catch (Exception e) {
                throw new TransactionSerialException("Failed to serialize newRows2 list");
            }
        } else {
            newRow2Bytes = new byte[1];
            newRow2Bytes[0] = " ".getBytes("UTF-8")[0];
        }
        finalBytes = new byte[stringBytes.length + selectBytes.length + oldvaluesBytes.length + resultsBytes.length +
                putBytes.length + put2Bytes.length + whereBytes.length + where2Bytes.length + newrowsBytes.length + newRow2Bytes.length + 28];
        try {
            finalBytes[0] = transaction.getHeader();
            int count = 1;
            for(int i = 0; i < stringBytes.length; i++){
                finalBytes[count] = stringBytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < selectBytes.length; i++){
                finalBytes[count] = selectBytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < oldvaluesBytes.length; i++){
                finalBytes[count] = oldvaluesBytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < resultsBytes.length; i++){
                finalBytes[count] = resultsBytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < putBytes.length; i++){
                finalBytes[count] = putBytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < put2Bytes.length; i++){
                finalBytes[count] = put2Bytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < whereBytes.length; i++){
                finalBytes[count] = whereBytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < where2Bytes.length; i++){
                finalBytes[count] = where2Bytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < newrowsBytes.length; i++){
                finalBytes[count] = newrowsBytes[i];
                count++;
            }
            finalBytes[count] = "<".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = "b".getBytes("UTF-8")[0];
            count++;
            finalBytes[count] = ">".getBytes("UTF-8")[0];
            count++;
            for(int i = 0; i < newRow2Bytes.length; i++){
                finalBytes[count] = newRow2Bytes[i];
                count++;
            }
        } catch (Exception e) {
            throw new TransactionSerialException("Failed to serialize transaction");
        }
        return finalBytes;
    }

    /**
     * Deserialize Transaction object from byte[].
     * Returns null if incorrect header;
     * @param serial
     * @Overide
     * @return
     * @throws Exception
     */
    @Override
    public Transaction deserialize(byte[] serial) throws SerialException, Exception {
        //System.out.println(new String(serial, "UTF-8"));
        Transaction transaction = new Transaction();
        byte[] stringBytes, selectBytes, oldvaluesBytes, resultsBytes, putBytes, put2Bytes, whereBytes, where2Bytes, newrowsBytes, newRow2Bytes;
        byte[] originalBytes = new byte[serial.length - 1];
        int count = 0, count2 = 0, stringLength = 0, selectLength = 0, oldValuesLength = 0, resultsLength = 0, putLength = 0,
            put2Length = 0, whereLength = 0, where2Length = 0, newRowsLength = 0, newRows2Length = 0;
        boolean skip = false;
        for(int i = 1; i < serial.length; i++){
            if(serial[i] == "<".getBytes("UTF-8")[0] && serial[i + 1] == "b".getBytes("UTF-8")[0] && serial[i + 2] == ">".getBytes("UTF-8")[0]){
                skip = true;
                if(stringLength == 0){
                    stringBytes = new byte[count2];
                    stringLength = i;
                    stringBytes = extractBytes(serial, 1, stringBytes);
                    //System.out.println(new String(stringBytes, "UTF-8"));
                    processStrings(stringBytes, transaction);
                } else if(selectLength == 0){
                    selectBytes = new byte[count2];
                    selectLength = i;
                    selectBytes = extractBytes(serial, stringLength + 3, selectBytes);
                    if(selectBytes.length != 1){
                        transaction.setSelect(deserializeList(selectBytes));
                    }
                } else if(oldValuesLength == 0){
                    oldvaluesBytes = new byte[count2];
                    oldValuesLength = i;
                    oldvaluesBytes = extractBytes(serial, selectLength + 3, oldvaluesBytes);
                    if(oldvaluesBytes.length != 1){
                        transaction.setOldValues(deserializeListMapIntMap(oldvaluesBytes));
                    }
                } else if(resultsLength == 0){
                    resultsBytes = new byte[count2];
                    resultsLength= i;
                    resultsBytes = extractBytes(serial, oldValuesLength + 3, resultsBytes);

                    if(resultsBytes.length != 1){
                        transaction.setResults(deserializeListMap(resultsBytes));
                    }
                } else if(putLength == 0){
                    putBytes = new byte[count2];
                    putLength = i;
                    putBytes = extractBytes(serial, resultsLength + 3, putBytes);
                    if(putBytes.length != 1){
                        transaction.setPut(deserializeMap(putBytes));
                    }
                } else if(put2Length == 0){
                    put2Bytes = new byte[count2];
                    put2Length = i;
                    put2Bytes = extractBytes(serial, putLength + 3, put2Bytes);
                    if(put2Bytes.length != 1){
                        transaction.setPut(deserializeMap(put2Bytes));
                    }
                } else if(whereLength == 0){
                    whereBytes = new byte[count2];
                    whereLength = i;
                    whereBytes = extractBytes(serial, put2Length + 3, whereBytes);
                    if(whereBytes.length != 1){
                        transaction.setWhere(deserializeMap(whereBytes));
                    }
                } else if(where2Length == 0){
                    where2Bytes = new byte[count2];
                    where2Length = i;
                    where2Bytes = extractBytes(serial, whereLength + 3, where2Bytes);
                    if(where2Bytes.length != 1){
                        transaction.setWhere2(deserializeMap(where2Bytes));
                    }
                } else if(newRowsLength == 0){
                    newrowsBytes = new byte[count2];
                    newRowsLength= i;
                    newrowsBytes = extractBytes(serial, where2Length + 3, newrowsBytes);
                    if(newrowsBytes.length != 1){
                        transaction.setNewRows(deserializeListMap(newrowsBytes));
                    }
                    newRow2Bytes = new byte[serial.length - (i + 3)];
                    newRow2Bytes = extractBytes(serial, i + 3, newRow2Bytes);
                    //if(newRow2Bytes.length != 1){
                    if(false){
                        transaction.setNewRows2(deserializeListMap(newRow2Bytes));
                    }
                    break;
                }
                count2 = 0;
            } else if(serial[i] == ">".getBytes("UTF-8")[0] && skip){
                skip = false;
            }else {
                if(!skip){
                    count2++;
                }
            }
        }
        return transaction;
    }

    private static void processStrings(byte[] bytes, Transaction transaction) throws Exception{
        String temp = new String(bytes, "UTF-8");
        String[] strings = temp.split("~");
        if(strings[0].equals("true")){
            transaction.setSuccessfull(true);
        }else{
            transaction.setSuccessfull(false);
        }
        transaction.setUsername(strings[1]);

        transaction.setOperation(strings[2]);
        transaction.setRequestTime(Long.parseLong(strings[3]));
        transaction.setFinishTime(Long.parseLong(strings[4]));
        transaction.setHowMany(new BigInteger(strings[5]));
        transaction.setFailMessage(strings[6]);
        transaction.setUserIndex(new BigInteger(strings[7]));
        transaction.setTransactionID(new BigInteger(strings[8]));
        transaction.setPassword(new BigInteger(strings[9]));
    }

    private static byte[] extractBytes(byte[] original, int start, byte[] result){
        int count = 0;
        for(int i = start; i < (start + result.length); i++){
            result[count] = original[i];
            count++;
        }
        return result;
    }

    public byte[] serializeObject(Object object) throws SerialException{
        if(object != null){
            byte[] bytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = null;
                out = new ObjectOutputStream(bos);
                out.writeObject(object);
                out.close();
                bos.close();
                bytes = bos.toByteArray();
            } catch (Exception e) {
                throw new SerialException("Failed to serialize object");
            }
            return bytes;
        }else{
            return null;
        }
    }

    /**
     * Deserialize implements Serializable Object from byte[].
     * @param bytes
     * @return
     * @throws SerialException
     */
    public List<Map<String, String>> deserializeListMap(byte[] bytes) throws SerialException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        List<Map<String, String>> o;
        try {
            in = new ObjectInputStream(bis);
            o = (List<Map<String, String>>)in.readObject();
            in.close();
            bis.close();;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerialException("Failed to deserialize object");
        }
        return o;
    }

    /**
     * Deserialize implements Serializable Object from byte[].
     * @param bytes
     * @return
     * @throws SerialException
     */
    public List<String> deserializeList(byte[] bytes) throws SerialException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        List<String> o;
        try {
            in = new ObjectInputStream(bis);
            o = (List<String>)in.readObject();
            in.close();
            bis.close();;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerialException("Failed to deserialize object");
        }
        return o;
    }

    /**
     * Deserialize implements Serializable Object from byte[].
     * @param bytes
     * @return
     * @throws SerialException
     */
    public List<Map<BigInteger, Map<String, String>>> deserializeListMapIntMap(byte[] bytes) throws SerialException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        List<Map<BigInteger, Map<String, String>>> o;
        try {
            in = new ObjectInputStream(bis);
            o = (List<Map<BigInteger, Map<String, String>>>)in.readObject();
            in.close();
            bis.close();;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerialException("Failed to deserialize object");
        }
        return o;
    }

    /**
     * Deserialize implements Serializable Object from byte[].
     * @param bytes
     * @return
     * @throws SerialException
     */
    public Map<String, String> deserializeMap(byte[] bytes) throws SerialException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        Map<String, String> o;
        try {
            in = new ObjectInputStream(bis);
            o = (Map<String, String>)in.readObject();
            in.close();
            bis.close();;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SerialException("Failed to deserialize object");
        }
        return o;
    }
}
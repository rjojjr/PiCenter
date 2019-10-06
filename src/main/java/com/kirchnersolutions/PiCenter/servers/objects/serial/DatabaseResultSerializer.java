package com.kirchnersolutions.PiCenter.servers.objects.serial;



import com.kirchnersolutions.PiCenter.dev.DevelopmentException;
import com.kirchnersolutions.PiCenter.servers.objects.Results;

import java.io.*;
import java.util.*;

public class DatabaseResultSerializer implements Serializer<Results> {

    @Override
    public byte[] serialize(Results results) throws Exception {
        byte[] nameBytes, valueBytes, typeBytes;

        byte[] finalBytes;
        String string = new String("");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        if (results.isSuccess()) {
            string = "true";
            nameBytes = string.getBytes("UTF-8");
        } else {
            string = ("false");
            nameBytes = string.getBytes("UTF-8");
        }
        valueBytes = results.getMessage().getBytes("UTF-8");
        String breakChar = "";
        string += breakChar;
        string += results.getMessage();
        if (results.getResults() != null && !results.getResults().isEmpty()) {
            try {
                typeBytes = serializeObject((results.getResults()));
            } catch (Exception e) {
                throw new Exception("Failed to serialize results");
            }
        } else {
            typeBytes = " ".getBytes("UTF-8");
        }
        try {
            byte[] serial = new byte[nameBytes.length + typeBytes.length + valueBytes.length + 2];
            int count = 0;
            for (int i = 0; count < nameBytes.length; count++) {
                serial[count] = nameBytes[count];
            }
            byte breakTag = "~".getBytes("UTF-8")[0];
            /*
            serial[count] = breakTag;
            count++;
            breakTag = "b".getBytes("UTF-8")[0];
            serial[count] = breakTag;
            count++;
            breakTag = ">".getBytes("UTF-8")[0];

             */
            serial[count] = breakTag;
            count++;
            for (int i = 0; i < valueBytes.length; count++) {
                serial[count] = valueBytes[i];
                i++;
            }
            /*
            breakTag = "<".getBytes("UTF-8")[0];
            serial[count] = breakTag;
            count++;
            breakTag = "b".getBytes("UTF-8")[0];
            serial[count] = breakTag;
            count++;
            breakTag = ">".getBytes("UTF-8")[0];

             */
            serial[count] = breakTag;
            count++;
            for (int i = 0; i < typeBytes.length; count++) {
                serial[count] = typeBytes[i];
                i++;
            }
            //System.out.println("here");
            finalBytes = new byte[serial.length + 1];
            finalBytes[0] = "4".getBytes("UTF-8")[0];
            for (int i = 0; i < serial.length; i++) {
                finalBytes[i + 1] = serial[i];
            }
            //System.out.println(new String(finalBytes, "UTF-8"));
            return finalBytes;
        } catch (ArrayIndexOutOfBoundsException ex) {

            //Rollback imp

            System.out.println("Serial index count is off");
            System.out.println(ex);
            throw new DevelopmentException("Serial index count is off: Class Field Method serialize()");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Results deserialize(byte[] serial) throws Exception {
        Results results = new Results();
        if (serial[0] != "4".getBytes("UTF-8")[0]) {
            return null;
        }
        byte[] originalBytes = new byte[serial.length - 1];
        for (int i = 0; i < originalBytes.length; i++) {
            originalBytes[i] = serial[i + 1];
        }
        serial = originalBytes;
        byte[] nameBytes, valueBytes, typeBytes;
        char breakTag = '\n';
        byte bTag = "~".getBytes("UTF-8")[0];
        try {
            boolean first = true;
            int count = -1, nameLength = 0, valueLength = 0, typeLength = 0;
            for (int i = 0; i < serial.length; i++) {
                if (serial[i] == bTag) {
                    if (nameLength == 0) {
                        nameLength = i;
                        first = false;
                    } else if (valueLength == 0) {
                        valueLength = count;
                        break;
                    }
                }
                if (!first) {
                    count++;
                }
            }
            typeLength = serial.length - (nameLength + valueLength + 2);
            nameBytes = new byte[nameLength];
            valueBytes = new byte[valueLength];
            typeBytes = new byte[typeLength];
            count = 1;
            for (int i = 0; i < nameLength; i++) {
                nameBytes[i] = serial[i];
                count++;
            }
            int count1 = 0;
            for (int i = count; count1 < valueLength; i++) {
                valueBytes[count1] = serial[i];
                count++;
                count1++;
            }
            count1 = 0;
            count++;
            for (int i = count; count1 < typeLength; i++) {
                typeBytes[count1] = serial[i];
                count++;
                count1++;
            }
            try {
                String suc = new String(nameBytes, "UTF-8");
                if (suc.equals("true")) {
                    results.setSuccess(true);
                } else {
                    results.setSuccess(false);
                }
                suc = new String(valueBytes, "UTF-8");
                //System.out.println(suc);
                results.setMessage(suc);
                if(typeBytes.length < 10){
                    results.setResults(new ArrayList<Map<String, String>>());
                }else{
                    results.setResults((List<Map<String, String>>) deserializeObject(typeBytes));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        /*
        byte[] originalBytes = new byte[serial.length - 1];
        for (int i = 0; i < originalBytes.length; i++) {
            originalBytes[i] = serial[i + 1];
        }
        DatabaseResults result = new DatabaseResults();
        String string = new String(originalBytes, "UTF-8");
        String breakChar = "~";
        List<String> strings = new ArrayList<>(Arrays.asList(string.split(breakChar + "")));
        System.out.println(strings.size());
        int count = 0;
        for (String field : strings) {
            switch (count) {
                case 0:
                    if (field.toString().equals("true")) {
                        result.setSuccess(true);
                    } else {
                        result.setSuccess(false);
                    }
                    count++;
                    break;
                case 1:
                    result.setMessage(field);

                    count++;
                    break;
                case 2:
                    if (field.length() < 2) {
                        result.setResults(null);

                    } else {
                        result.setResults((List<Map<String, String>>) deserializeObject(field.getBytes("UTF-8")));
                    }
                    count++;
                    break;
            }
        }
        return result;

         */
        } catch (Exception e) {
            e.printStackTrace();

        }
        return results;
    }

    /**
     * Serialize implements Serializable Object into byte[].
     * @param object
     * @return
     * @throws Exception
     */

    public byte[] serializeObject(Object object) throws Exception{
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
                bytes = Base64.getEncoder().encode(bytes);
            } catch (Exception e) {
                throw new Exception("Failed to serialize object");
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
     * @throws Exception
     */
    public List<Map<String, String>> deserializeObject(byte[] bytes) throws Exception{
        bytes = Base64.getDecoder().decode(bytes);
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
            throw new Exception("Failed to deserialize object");
        }
        return o;
    }
}
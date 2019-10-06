package com.kirchnersolutions.PiCenter.servers.objects.serial;
/**
 *2019 Kirchner Solutions
 * @Author Robert Kirchner Jr.
 *
 * This code may not be decompiled, recompiled, copied, redistributed or modified
 * in any way unless given express written consent from Kirchner Solutions.
 */

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Serials will have one byte int header.
 * 1 = transaction.
 * 2 = field.
 * 3 = user.
 * 4 = config.
 * -1 = general
 * @param <A>
 */
public interface Serializer<A> {

    /**
     * Serialize implements Serializable Object into byte[].
     * @param object
     * @return
     * @throws Exception
     */
    default byte[] serializeObject(Object object) throws Exception{
        if(object != null){
            byte[] bytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = null;
                ByteBuffer buffer;
                out = new ObjectOutputStream(bos);
                out.writeObject(object);
                out.close();
                bos.close();
                bytes = bos.toByteArray();
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
    default Object deserializeObject(byte[] bytes) throws Exception{
        Object o;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in = new ObjectInputStream(bis);
            o = (Object)in.readObject();
            in.close();
            bis.close();;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to deserialize object");
        }
        return o;
    }

    /**
     * Serialize A Object into byte[].
     * @param object
     * @return
     * @throws Exception
     */
    byte[] serialize(A object) throws Exception;

    /**
     * Deserialize A Object from byte[].
     * @param bytes
     * @return
     * @throws Exception
     */
    A deserialize(byte[] bytes) throws Exception;

}
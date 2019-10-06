package com.kirchnersolutions.PiCenter.servers.objects.serial;
/**
 *2019 Kirchner Solutions
 * @Author Robert Kirchner Jr.
 *
 * This code may not be decompiled, recompiled, copied, redistributed or modified
 * in any way unless given express written consent from Kirchner Solutions.
 */
import com.kirchnersolutions.database.exceptions.SerialException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
public class GeneralSerializer implements Serializer<Object>{

    /**
     * Invokes serializeObject method in interface
     * @param object
     * @return
     * @throws SerialException
     */
    @Override
    public byte[] serialize(Object object) throws SerialException {
        return serializeObject(object);
    }

    /**
     * Invokes deserializeObject method in interface
     * @param serial
     * @return
     * @throws SerialException
     */
    @Override
    public Object deserialize(byte[] serial) throws SerialException {
        return deserializeObject(serial);
    }

}
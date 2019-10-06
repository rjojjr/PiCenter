package com.kirchnersolutions.PiCenter.servers.objects.serial;
/**
 *2019 Kirchner Solutions
 * @Author Robert Kirchner Jr.
 *
 * This code may not be decompiled, recompiled, copied, redistributed or modified
 * in any way unless given express written consent from Kirchner Solutions.
 */


public class ConfigSerializer implements Serializer<String[]> {

    @Override
    public byte[] serialize(String[] vars) throws Exception {
        String string = new String();
        boolean first = true;
        for(String var : vars){
            if(first){
                string+= (var);
                first = false;
            }else{
                string+= ('%');
                string+= (var);
            }
        }
        return string.toString().getBytes("UTF-8");
    }

    @Override
    public String[] deserialize(byte[] bytes) throws Exception {
        String string = new String(bytes, "UTF-8");
        String[] results = string.split("%");
        return results;
    }
}
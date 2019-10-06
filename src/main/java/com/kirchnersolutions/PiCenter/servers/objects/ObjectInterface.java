package com.kirchnersolutions.PiCenter.servers.objects;

public interface ObjectInterface {

    /**
     * Serials will have one byte int header.
     * 1 = transaction.
     * 2 = field.
     * 3 = user.
     * 4 = config.
     * -1 = general
     * @return
     */
    public byte getHeader() throws Exception;

}
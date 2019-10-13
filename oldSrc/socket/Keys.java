package com.kirchnersolutions.PiCenter.servers.socket;

import com.kirchnersolutions.utilities.CryptTools;

import javax.crypto.SecretKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

public class Keys {

    private Map<String, Object> RSAKeys;
    private SecretKey secretKey = null;
    private PublicKey publicKey = null;

    void generateRSAKeys() throws Exception{
        RSAKeys = CryptTools.getRSAKeys();
    }

    boolean getPublicKey(byte[] key) throws Exception{
        publicKey = CryptTools.deserializePubKey(key);
        if(publicKey == null){
            return false;
        }
        return true;
    }

    byte[] encryptAESKey() {
        if (publicKey == null) {
            return new byte[0];
        }
        try{
            secretKey = CryptTools.generateRandomSecretKey();
            return CryptTools.encryptRSAMsg(CryptTools.serializeAESKey(secretKey), publicKey);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    byte[] decryptAESResponse(String response) throws Exception{
        if(secretKey == null){
            return null;
        }
        return CryptTools.aesDecrypt(secretKey, Base64.getDecoder().decode(response));
    }

    byte[] encryptAESRequest(byte[] request) throws Exception{
        if(secretKey == null){
            return null;
        }
        return CryptTools.aesEncrypt(secretKey, Base64.getEncoder().encode(request));
    }

}

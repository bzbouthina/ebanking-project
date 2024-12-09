package com.myproject.myapp.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class EncryptionUtil {
    //بعدين خزني الرمز متعك في 
    //config or env or even in application.yml
    //لان هنا مش سكيور
    private static final String ENCRYPTION_SECRET_KEY= "s3cr3tK3y1234567";
    private static final String ALGORITHM = "AES";
    private static final SecretKey secretKey = new SecretKeySpec(ENCRYPTION_SECRET_KEY.getBytes(), ALGORITHM);
  
    //عملية التشفير
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    //فك التشفير
    public static String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}

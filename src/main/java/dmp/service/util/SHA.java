package dmp.service.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
    public static String encrypt(String input) {
        
        String sha = null;
        
        if(null == input) return null;
        
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            
            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());
            
            //Converts message digest value in base 16 (hex)
            sha = new BigInteger(1, digest.digest()).toString(16);
            
        } catch (NoSuchAlgorithmException e) {
        }
        
        return sha;
    }
}

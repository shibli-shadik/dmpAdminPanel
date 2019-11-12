package dmp.service.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.util.Base64;

/**
 * @author Shibli Shadik Osmani
 * @date 10-Sep-2017
 */
public class TripleDES {
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private final KeySpec myKeySpec;
    private final SecretKeyFactory mySecretKeyFactory;
    private final Cipher cipher;
    byte[] keyAsBytes;
    private final String myEncryptionKey;
    private final String myEncryptionScheme;
    SecretKey key;
    
    public TripleDES() throws Exception
    {
        //TripleDES does not use those key sizes. It uses 168 bit (DES uses 56 bit, and TripleDES uses 3 times that - hence its name).
        myEncryptionKey = "iTcl#Qc@shm0bile2017@pp$"; //minimum 24 char required
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        myKeySpec = new DESedeKeySpec(keyAsBytes);
        mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = mySecretKeyFactory.generateSecret(myKeySpec);
    }
    
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString = Base64.getEncoder().encodeToString(encryptedText);
            
        } catch (UnsupportedEncodingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
        }
        return encryptedString;
    }
    
    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.getDecoder().decode(encryptedString);//base64decoder.decodeBuffer(encryptedString);
            
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= bytes2String(plainText);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
        }
        return decryptedText;
    }
    
    private static String bytes2String(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringBuilder.append((char) bytes[i]);
        }
        
        return stringBuilder.toString();
    }
}

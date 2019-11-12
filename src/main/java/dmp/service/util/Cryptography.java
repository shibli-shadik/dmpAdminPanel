package dmp.service.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Cryptography
{
    private static final Logger LOGGER = LogManager.getLogger(Cryptography.class);
    static final String HEXES = "0123456789ABCDEF";
    
    public byte[] hexToByteArray(String hex, int size)
    {
        Map<String, Byte> hexindex = new HashMap<>();
        
        for(int i = 0; i <= 255; i++)
        {
            if(i < 16)
            {
                hexindex.put("0" + Integer.toHexString(i).toUpperCase(), (byte)i);
            }
            else
            {
                hexindex.put(Integer.toHexString(i).toUpperCase(), (byte)i);
            }
        }
        
        byte[] hexres = new byte[size];
        
        for(int i = 0, j =0 ; i < hex.length(); i += 2, j++)
        {
            hexres[j] = hexindex.get(hex.substring(i, i+2));
        }
        
        return hexres;
    }
    
    public byte[] tripleDesEncrypt(byte[] plainTextBytes, byte[] keyValue)
    {
        byte[] cipherText = null;
        
        try
        {
            final SecretKey key = new SecretKeySpec(keyValue, "DESede");
            final Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            cipherText = cipher.doFinal(plainTextBytes);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex)
        {
            LOGGER.error(ex.getMessage());
        }
        
        return cipherText;
    }
    
    public byte[] tripleDesDecrypt(byte[] message, byte[] keyValue)
    {
        byte[] plainText = null;
        
        try
        {
            final SecretKey key = new SecretKeySpec(keyValue, "DESede");
            final Cipher decipher = Cipher.getInstance("DESede/ECB/NoPadding");
            decipher.init(Cipher.DECRYPT_MODE, key);
            
            plainText = decipher.doFinal(message);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex)
        {
            LOGGER.error(ex.getMessage());
        }
        
        return plainText;
    }
    
    public String getAnsiPinBlockFormat(String card, String clearPIN)
    {
        String tmpPinBlock = "";
        String tmpCardBlock = "";
        
        if(card.length() == 18)
        {
            tmpPinBlock = "04" + String.format("%1$-" + 4 + "s", clearPIN).replace(' ', '0') + "FFFFFFFFFFFF";
            tmpCardBlock = "0000" + card.substring(5, 17);
        }
        else if(card.length() == 16)
        {
            tmpPinBlock = "04" + String.format("%1$-" + 4 + "s", clearPIN).replace(' ', '0') + "FFFFFFFFFF";
            tmpCardBlock = "0000" + card.substring(3, 15);
        }
        
        return performXOR(tmpCardBlock, tmpPinBlock);
    }
    
    public String performXOR(String val1, String val2)
    {
        String binVal = "";
        String binVa2 = "";
        String binResult = "";
        String hexVal = "";
        int i = 0;
        
        for (i = 0; i < val1.length(); i++)
        {
            binVal = binVal + hexToBinary(String.valueOf(val1.charAt(i)));
        }
        
        for (i = 0; i < val2.length(); i++)
        {
            binVa2 = binVa2 + hexToBinary(String.valueOf(val2.charAt(i)));
        }
        
        for (i = 0; i < binVal.length(); i++)
        {
            binResult = binResult+((int)binVal.charAt(i) ^ (int)binVa2.charAt(i));
        }
        
        for (i = 0; i < binResult.length(); i = i + 4)
        {
            hexVal = hexVal + binaryToHex(binResult.substring(i, i+4));
        }
        
        return hexVal;
    }
    
    public String hexToBinary(String hex)
    {
        switch (hex)
        {
            case "0":
                return "0000";
            case "1":
                return "0001";
            case "2":
                return "0010";
            case "3":
                return "0011";
            case "4":
                return "0100";
            case "5":
                return "0101";
            case "6":
                return "0110";
            case "7":
                return "0111";
            case "8":
                return "1000";
            case "9":
                return "1001";
            case "A":
                return "1010";
            case "B":
                return "1011";
            case "C":
                return "1100";
            case "D":
                return "1101";
            case "E":
                return "1110";
            case "F":
                return "1111";
            default:
                break;
        }
        
        return "0000";
    }
    
    public String binaryToHex(String hex)
    {
        switch (hex)
        {
            case "0000":
                return "0";
            case "0001":
                return "1";
            case "0010":
                return "2";
            case "0011":
                return "3";
            case "0100":
                return "4";
            case "0101":
                return "5";
            case "0110":
                return "6";
            case "0111":
                return "7";
            case "1000":
                return "8";
            case "1001":
                return "9";
            case "1010":
                return "A";
            case "1011":
                return "B";
            case "1100":
                return "C";
            case "1101":
                return "D";
            case "1110":
                return "E";
            case "1111":
                return "F";
            default:
                break;
        }
        
        return "0";
    }
    
    public String getHex( byte [] raw )
    {
        if ( raw == null )
        {
            return null;
        }
        
        final StringBuilder hex = new StringBuilder( 2 * raw.length );
        
        for ( final byte b : raw )
        {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }
        
        return hex.toString();
    }
}

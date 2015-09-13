package util;

import java.lang.Integer;
import java.lang.StringBuilder;
import java.security.*;
import java.util.*;
import play.Logger;

public class CryptoUtil {
    public static String generateSalt()
    {
        String salt = null;
        
        try {
            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
            byte saltBytes[] = new byte[16];
            rand.nextBytes(saltBytes);
            
            salt = saltBytes.toString();
        } catch (NoSuchAlgorithmException e) {
            Logger.error("Error generating salt. %s", e.getMessage());
        }
        
        return salt;
    }
    
    public static String hashPassword(String password, String salt)
    {
        String hash = null;
        
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(salt.getBytes());
            
            byte hashBytes[] = digest.digest(password.getBytes());
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < hashBytes.length; ++i) {
                String hexval = Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1);
                strBuilder.append(hexval);
            }
            
            hash = strBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            Logger.error("Error generating password hash. %s", e.getMessage());
        }
        
        return hash;
    }
}

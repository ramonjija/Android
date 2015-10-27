package ramonsilva.controledegeladeira;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ramon.silva on 27/10/2015.
 */
public class HashUtils {
    private HashUtils() {

    }

    public static String generateMD5(String message) throws Exception {
        return hashString(message, "MD5");
    }

    public static String generateSHA1(String message) throws Exception {
        return hashString(message, "SHA-1");
    }

    public static String generateSHA256(String message) throws Exception {
        return hashString(message, "SHA-256");
    }

    private static String hashString(String message, String algorithm) throws Exception {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new Exception(
                    "não foi possível gerar hash para string", ex);
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }



}

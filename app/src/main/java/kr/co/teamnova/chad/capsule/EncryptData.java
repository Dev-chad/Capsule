package kr.co.teamnova.chad.capsule;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Chad on 2017-01-16.
 */

public class EncryptData {


    // TODO: Analysis about that how to generate SHA256
    public String getSHA256(String plainPassword) {
        String cipherPassword = "";

        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(plainPassword.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            cipherPassword = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cipherPassword = null;
        }
        return cipherPassword;
    }

}

package kr.co.teamnova.chad.capsule;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chad on 2017-02-20.
 */

public class Utils {

    static String getSHA256(String plainPassword) {
        String cipherPassword;

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

    public static String getStringFromByteString(String target, String regex) {
        String[] strArray = target.split(regex);

        byte[] byteArray = new byte[strArray.length];

        for (int i = 0; i < strArray.length; i++) {
            byteArray[i] = Byte.valueOf(strArray[i]);
        }

        return new String(byteArray);
    }

    public static String getByteStringForm(String origin, String separator) {
        byte[] array = origin.getBytes();
        String strByteArray = String.valueOf(array[0]);

        for (int i = 1; i < array.length; i++) {
            strByteArray += (separator + array[i]);
        }

        return strByteArray;
    }

    public static String getTime(long savedTime) {
        String date = "";
        long currentTime = System.currentTimeMillis();
        long subTime = currentTime - savedTime;

        if (subTime / 60000 < 1) {
            date = "방금 전";
        } else if (subTime / 60000 < 60) {
            date = (subTime / 60000) + "분 전";
        } else if (subTime / 3600000 < 24) {
            date = (subTime / 3600000) + "시간 전";
        } else if (subTime / 86400000 < 7) {
            date = (subTime / 86400000) + "일 전";
        } else {
            date = new SimpleDateFormat("yyyy년 MM월 dd일  HH:mm").format(new Date(savedTime));
        }
        return date;
    }
}

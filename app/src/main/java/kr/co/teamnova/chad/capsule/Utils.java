package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;
import static kr.co.teamnova.chad.capsule.ContentListViewAdapter.calculateInSampleSize;

/**
 * Created by Chad on 2017-02-20.
 */

public class Utils {

    public static SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
    public static SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH시 mm분", Locale.KOREA);

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

    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri uriImage) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uriImage), null, options);
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            Log.d(TAG, "Original: " + options.outWidth + "x" + options.outHeight);
//            Log.d(TAG, "View width: " + dm.widthPixels);
            options.inSampleSize = calculateInSampleSize(options, dm.widthPixels);
            options.inJustDecodeBounds = false;
            Log.d(TAG, "inSampleSize: " + options.inSampleSize);
            Bitmap bp = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uriImage), null, options);
            Log.d(TAG, "Sampled: " + bp.getWidth() + "x" + bp.getHeight());
            return bp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}

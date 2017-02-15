package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

/**
 * Created by Chad on 2017-01-15.
 */

public class LogoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                SharedPreferences spAppPrefs = getSharedPreferences("app", MODE_PRIVATE);

                if (spAppPrefs.getBoolean("auto_login_use", false)) {
                    String email = spAppPrefs.getString("auto_login_email", "");
                    SharedPreferences spAccount = getSharedPreferences("account", MODE_PRIVATE);

                    if (spAccount.contains(email)) {
                        String[] strUserData = spAccount.getString(email, "").split(",");

                        if (strUserData[Const.INDEX_PASSWORD].equals(spAppPrefs.getString("auto_login_password", ""))) {
                            User loginUser = new User(email, strUserData);
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("login_user", loginUser);
                        } else {
                            intent = new Intent(getApplicationContext(), LoginPageActivity.class);
                        }
                    } else {
                        intent = new Intent(getApplicationContext(), LoginPageActivity.class);
                    }
                } else {
                    intent = new Intent(getApplicationContext(), LoginPageActivity.class);
                }

//                createTestAccount();
                startActivity(intent);

                finish();
            }
        }, 2000);
    }

    public void createTestAccount() {

        SharedPreferences spAccount = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor spAccountEditor = spAccount.edit();

        String name = "tester";
        String password = "test";
        String phone = "01011111111";
        String profileImage = Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.image_user).toString();

        for (int i = 1; i <= 30; i++) {
            String email = "test" + i + "@test.com";
            String nickname = "test" + i;

            File file = new File(getFilesDir() + "/contents/" + email);
            try {
                file.mkdirs();
            } catch (Exception e) {
                Log.e("error", e.toString());
            }

            String strUserData =
                    EncryptData.getSHA256(password)+','
                            + name + ','
                            + phone + ','
                            + nickname + ','
                            + profileImage + ",0, , ";

            spAccountEditor.putString(email, strUserData);

        }

        spAccountEditor.apply();
    }
}

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

        final SharedPreferences spAutoLogin = getSharedPreferences("login_info", MODE_PRIVATE);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (spAutoLogin.getBoolean("enable", false) && spAutoLogin.getString("password", "1").equals(getSharedPreferences(spAutoLogin.getString("email", "trash"), MODE_PRIVATE).getString("password", "0"))) {
                    SharedPreferences sp = getSharedPreferences(spAutoLogin.getString("email", ""), MODE_PRIVATE);

                    User loginUser = new User(spAutoLogin.getString("email", ""), sp.getString("nickname", ""), sp.getString("phone", ""), Uri.parse(sp.getString("profile_image", "")));
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("login_user", loginUser);
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
        for (int i = 1; i <= 30; i++) {
            String email = "test" + i + "@test.com";
            String nickname = "test" + i;
            String firstname = "test" + i;
            String lastname = "test" + i;
            String password = "test";
            String phone = "01011111111";

            SharedPreferences profileData = getSharedPreferences(email, MODE_PRIVATE);
            SharedPreferences nickNameData = getSharedPreferences("nickname_info", MODE_PRIVATE);
            SharedPreferences emailData = getSharedPreferences("email_info", MODE_PRIVATE);
            SharedPreferences.Editor profileEditor = profileData.edit();
            SharedPreferences.Editor nicknameEditor = nickNameData.edit();
            SharedPreferences.Editor emailEditor = emailData.edit();

            emailEditor.putString(email, ""+System.currentTimeMillis());
            profileEditor.putString("first_name", firstname);
            profileEditor.putString("last_name", lastname);
            profileEditor.putString("nickname", nickname);
            nicknameEditor.putString(nickname, "");
            profileEditor.putString("phone", phone);
            profileEditor.putString("email", email);
            profileEditor.putString("password", EncryptData.getSHA256(password));
            profileEditor.putString("profile_image", Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.ic_launcher).toString());
            profileEditor.putInt("num_of_content", 0);

            File file = new File("/data/data/" + getPackageName() + "/User/" + email + "/Contents");
            try {
                file.mkdirs();
            } catch (Exception e) {
                Log.e("error", e.toString());
            }

            emailEditor.apply();
            nicknameEditor.apply();
            profileEditor.apply();
        }
    }
}

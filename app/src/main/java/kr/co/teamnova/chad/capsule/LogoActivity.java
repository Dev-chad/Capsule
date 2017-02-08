package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
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
                if(spAutoLogin.getString("enable", "").equals("true") && new File("/data/data/" + getPackageName() + "/shared_prefs/" + spAutoLogin.getString("email","") + ".xml").exists()){
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("email", spAutoLogin.getString("email", ""));
                }else{
                    intent = new Intent(getApplicationContext(), LoginPageActivity.class);
                }
//                createTestAccount();
                startActivity(intent);

                finish();
            }
        }, 2000);
    }

    public void createTestAccount(){
        for(int i=1; i<30; i++){
            String email = "test"+i+"@test.com";
            String nickname = "test"+i;
            String firstname = "test"+i;
            String lastname = "test"+i;
            String password = "test";
            String phone = "01011111111";

            SharedPreferences profileData = getSharedPreferences(email, MODE_PRIVATE);
            SharedPreferences nickNameData = getSharedPreferences("Nickname", MODE_PRIVATE);
            SharedPreferences.Editor profileEditor = profileData.edit();
            SharedPreferences.Editor nicknameEditor = nickNameData.edit();

            profileEditor.putString("first_name",firstname);
            profileEditor.putString("last_name", lastname);
            profileEditor.putString("nickname", nickname);
            nicknameEditor.putString(nickname, "");
            profileEditor.putString("phone", phone);
            profileEditor.putString("email", email);
            profileEditor.putString("password", EncryptData.getSHA256(password));

            File file = new File("/data/data/" + getPackageName() + "/User/" + email + "/Contents");
            try{
                file.mkdirs();
            }catch (Exception e){
                Log.e("error", e.toString());
            }

            File userDir = new File("/data/data/" + getPackageName() + "/User/" + email);

            nicknameEditor.apply();
            profileEditor.apply();
        }
    }
}

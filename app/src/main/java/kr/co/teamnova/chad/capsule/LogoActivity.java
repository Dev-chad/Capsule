package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

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
                startActivity(intent);

                finish();
            }
        }, 2000);
    }
}

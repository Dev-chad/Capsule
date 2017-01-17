package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Chad on 2017-01-17.
 */

public class HomeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textEmail = (TextView) findViewById(R.id.text_email);
        TextView textFirstname = (TextView) findViewById(R.id.text_firstname);
        TextView textLastname = (TextView) findViewById(R.id.text_lastname);
        TextView textPassword = (TextView) findViewById(R.id.text_password);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        SharedPreferences profileData = getSharedPreferences(email, MODE_PRIVATE);
        textEmail.setText(email);
        textFirstname.setText(profileData.getString("first_name", ""));
        textLastname.setText(profileData.getString("last_name", ""));
        textPassword.setText(profileData.getString("password", ""));
    }
}

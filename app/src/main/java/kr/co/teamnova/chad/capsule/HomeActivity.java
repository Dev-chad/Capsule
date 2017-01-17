package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

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
        ImageView imageProfile = (ImageView) findViewById(R.id.image_profile);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        SharedPreferences profileData = getSharedPreferences(email, MODE_PRIVATE);
        textEmail.setText(email);
        textFirstname.setText(profileData.getString("first_name", ""));
        textLastname.setText(profileData.getString("last_name", ""));
        textPassword.setText(profileData.getString("password", ""));
        Uri uri = Uri.parse(profileData.getString("profile_image", ""));
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            imageProfile.setImageBitmap(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package kr.co.teamnova.chad.capsule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Chad on 2017-01-17.
 */

public class HomeActivity extends AppCompatActivity{
    private final int STATE_HOME = 0;
    private final int STATE_SEARCH = 1;
    private final int STATE_PEOPLE= 2;
    private final int STATE_ADD = 3;

    private int stateNum = 0;

    private ImageButton btnHome;
    private ImageButton btnSearch;
    private ImageButton btnPeople;
    private ImageButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnHome = (ImageButton) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_HOME;
                setButtonAlpha();
            }
        });

        btnSearch = (ImageButton) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_SEARCH;
                setButtonAlpha();
            }
        });

        btnPeople = (ImageButton) findViewById(R.id.btn_people);
        btnPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_PEOPLE;
                setButtonAlpha();
            }
        });

        btnAdd = (ImageButton) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateNum = STATE_ADD;
                setButtonAlpha();
            }
        });
        setButtonAlpha();

        /*TextView textEmail = (TextView) findViewById(R.id.text_email);
        TextView textFirstname = (TextView) findViewById(R.id.text_firstname);
        TextView textLastname = (TextView) findViewById(R.id.text_lastname);
        TextView textPassword = (TextView) findViewById(R.id.text_password);
        TextView textNickname = (TextView) findViewById(R.id.text_nickname);
        ImageView imageProfile = (ImageView) findViewById(R.id.image_profile);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        SharedPreferences profileData = getSharedPreferences(email, MODE_PRIVATE);
        textEmail.setText(email);
        textFirstname.setText(profileData.getString("first_name", ""));
        textLastname.setText(profileData.getString("last_name", ""));
        textNickname.setText(profileData.getString("nickname", ""));
        textPassword.setText(profileData.getString("password", ""));
        if(profileData.contains("profile_image")){
            Uri uri = Uri.parse(profileData.getString("profile_image", ""));
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageProfile.setImageBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageProfile.setImageResource(R.mipmap.ic_launcher);
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
//        setButtonAlpha();
    }

    private void setButtonAlpha(){
        switch (stateNum){
            case STATE_HOME:
                btnHome.setImageResource(R.mipmap.image_btn_home);
                btnSearch.setImageResource(R.mipmap.image_btn_search_inactive);
                btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                break;
            case STATE_SEARCH:
                btnHome.setImageResource(R.mipmap.image_btn_home_inactive);
                btnSearch.setImageResource(R.mipmap.image_btn_search);
                btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                break;
            case STATE_PEOPLE:
                btnHome.setImageResource(R.mipmap.image_btn_home_inactive);
                btnSearch.setImageResource(R.mipmap.image_btn_search_inactive);
                btnPeople.setImageResource(R.mipmap.image_btn_people);
                btnAdd.setImageResource(R.mipmap.image_btn_add_inactive);
                break;
            case STATE_ADD:
                btnHome.setImageResource(R.mipmap.image_btn_home_inactive);
                btnSearch.setImageResource(R.mipmap.image_btn_search_inactive);
                btnPeople.setImageResource(R.mipmap.image_btn_people_inactive);
                btnAdd.setImageResource(R.mipmap.image_btn_add);
                break;
        }
    }
}

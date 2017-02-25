package kr.co.teamnova.chad.capsule;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        User loginUser = getIntent().getParcelableExtra("login_user");
        User chatUser = getIntent().getParcelableExtra("chat_user");

        setTitle(chatUser.getNickname());

        Log.d(TAG, loginUser.getEmail()+ " " + chatUser.getEmail());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
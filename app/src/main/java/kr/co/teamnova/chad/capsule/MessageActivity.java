package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MessageActivity";
    private final int MODE_FOLLOW = 0;
    private final int MODE_CHAT = 1;

    private User loginUser;
    private ListView listFollow;
    private ListView listChat;
    private TextView textNoFollow;
    private TextView textNoChat;
    private ImageButton ibtnFollow;
    private ImageButton ibtnChat;

    private FollowListAdapter followListAdapter;

    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        textNoChat = (TextView)findViewById(R.id.text_no_chat);
        textNoFollow = (TextView)findViewById(R.id.text_no_follow);

        ibtnChat = (ImageButton)findViewById(R.id.ibtn_chat);
        ibtnChat.setOnClickListener(this);
        ibtnFollow = (ImageButton)findViewById(R.id.ibtn_follow);
        ibtnFollow.setOnClickListener(this);

        listFollow = (ListView)findViewById(R.id.list_follow);
        listChat = (ListView)findViewById(R.id.list_chat);
        listChat.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String strLoginUserEmail = getIntent().getStringExtra("login_user");
        SharedPreferences spAccount = getSharedPreferences("account", MODE_PRIVATE);
        String[] strUserData = spAccount.getString(strLoginUserEmail, "").split(",");
        loginUser = new User(strLoginUserEmail, strUserData);

        if(loginUser.getFollowList().size() == 0){
            textNoFollow.setVisibility(View.VISIBLE);
        } else {
            textNoFollow.setVisibility(View.GONE);
        }

        ArrayList<User> followList = new ArrayList<>();

        for (String email : loginUser.getFollowList()) {
            strUserData = spAccount.getString(email, "").split(",");
            followList.add(new User(email, strUserData));
        }

        followListAdapter = new FollowListAdapter(this, loginUser, followList);
        listFollow.setAdapter(followListAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.ibtn_chat:
                mode = MODE_CHAT;
                listFollow.setVisibility(View.GONE);
                listChat.setVisibility(View.VISIBLE);
                followListAdapter.setSelectedItem(-1);
                followListAdapter.notifyDataSetChanged();
                break;
            case R.id.ibtn_follow:
                mode = MODE_FOLLOW;
                listFollow.setVisibility(View.VISIBLE);
                listChat.setVisibility(View.GONE);
                if(loginUser.getFollowList().size() == 0){
                    textNoFollow.setVisibility(View.VISIBLE);
                } else {
                    textNoFollow.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ibtnChat.callOnClick();
    }
}

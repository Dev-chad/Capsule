package kr.co.teamnova.chad.capsule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class LikeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        ListView listViewUser;
        LikeUserListAdapter adapter;
        listViewUser = (ListView) findViewById(R.id.listView_like);
        ArrayList<User> userList = new ArrayList<>();

        SharedPreferences spAccount = getSharedPreferences("account", MODE_PRIVATE);
        String[] strUserData;

        for (String email : getIntent().getStringArrayListExtra("like_user_list")) {
            strUserData = spAccount.getString(email, "").split(",");
            userList.add(new User(email, strUserData));
        }

        adapter = new LikeUserListAdapter(userList);
        listViewUser.setAdapter(adapter);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}

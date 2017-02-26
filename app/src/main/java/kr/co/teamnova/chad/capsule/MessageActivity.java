package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
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
    private ChatListAdapter chatListAdapter;

    private ArrayList<User> followList;
    private ArrayList<ChatListItem> chatListItems;

    private SharedPreferences spAccount;

    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        textNoChat = (TextView) findViewById(R.id.text_no_chat);
        textNoFollow = (TextView) findViewById(R.id.text_no_follow);

        ibtnChat = (ImageButton) findViewById(R.id.ibtn_chat);
        ibtnChat.setOnClickListener(this);
        ibtnFollow = (ImageButton) findViewById(R.id.ibtn_follow);
        ibtnFollow.setOnClickListener(this);

        ibtnChat.setImageAlpha(30);

        listFollow = (ListView) findViewById(R.id.list_follow);
        listChat = (ListView) findViewById(R.id.list_chat);
        listChat.setVisibility(View.GONE);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String strLoginUserEmail = getIntent().getStringExtra("login_user");
        spAccount = getSharedPreferences("account", MODE_PRIVATE);
        String[] strUserData = spAccount.getString(strLoginUserEmail, "").split(",");
        loginUser = new User(strLoginUserEmail, strUserData);

        if (loginUser.getFollowList().size() == 0) {
            textNoFollow.setVisibility(View.VISIBLE);
        } else {
            textNoFollow.setVisibility(View.GONE);
        }

        followList = new ArrayList<>();

        for (String email : loginUser.getFollowList()) {
            strUserData = spAccount.getString(email, "").split(",");
            followList.add(new User(email, strUserData));
        }

        followListAdapter = new FollowListAdapter(this, loginUser, followList);
        listFollow.setAdapter(followListAdapter);

        chatListItems = new ArrayList<>();

        SharedPreferences spMessage = getSharedPreferences("message", MODE_PRIVATE);
        final SharedPreferences.Editor spMesseageEditor = spMessage.edit();
        Set<String> keySet = spMessage.getAll().keySet();

        for (String key : keySet) {
            if (key.contains(loginUser.getEmail() + ",")) {
                String email = key.split(loginUser.getEmail() + ",")[1];
                String totalMessage = spMessage.getString(key, "");
                User user = new User(email, spAccount.getString(email, "").split(","));
                int size = totalMessage.split(",").length;
                String[] lastMessage = totalMessage.split(",")[size - 1].split("::");

                ChatListItem chatListItem = new ChatListItem(user.getNickname(), user.getUriProfileImage(), Utils.getStringFromByteString(lastMessage[1], "\\+"), user.getEmail(), Long.valueOf(lastMessage[2]));
                chatListItems.add(chatListItem);

            }
        }

        Collections.sort(chatListItems, new FileNameSort());

        chatListAdapter = new ChatListAdapter(chatListItems);
        listChat.setAdapter(chatListAdapter);

        listChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatListItem chatListItem = chatListItems.get(position);
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                intent.putExtra("login_user", loginUser);
                intent.putExtra("chat_user", new User(chatListItem.getEmail(), spAccount.getString(chatListItem.getEmail(), "").split(",")));
                startActivityForResult(intent, 0);
            }
        });

       /*listChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               *//*ChatListItem chatListItem = chatListItems.get(position);
               spMesseageEditor.remove()*//*
           }
       });*/
        registerForContextMenu(listChat);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.ibtn_chat:
                mode = MODE_CHAT;
                ibtnFollow.setImageAlpha(30);
                ibtnChat.setImageAlpha(255);
                listFollow.setVisibility(View.GONE);
                listChat.setVisibility(View.VISIBLE);
                followListAdapter.setSelectedItem(-1);
                followListAdapter.notifyDataSetChanged();
                break;
            case R.id.ibtn_follow:
                mode = MODE_FOLLOW;
                ibtnChat.setImageAlpha(30);
                ibtnFollow.setImageAlpha(255);
                listFollow.setVisibility(View.VISIBLE);
                listChat.setVisibility(View.GONE);
                if (loginUser.getFollowList().size() == 0) {
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
        SharedPreferences spMessage = getSharedPreferences("message", MODE_PRIVATE);
        chatListItems.clear();
        Set<String> keySet = spMessage.getAll().keySet();

        for (String key : keySet) {
            if (key.contains(loginUser.getEmail() + ",")) {
                String email = key.split(loginUser.getEmail() + ",")[1];
                String totalMessage = spMessage.getString(key, "");
                User user = new User(email, spAccount.getString(email, "").split(","));
                int size = totalMessage.split(",").length;
                String[] lastMessage = totalMessage.split(",")[size - 1].split("::");

                ChatListItem chatListItem = new ChatListItem(user.getNickname(), user.getUriProfileImage(), Utils.getStringFromByteString(lastMessage[1], "\\+"), user.getEmail(), Long.valueOf(lastMessage[2]));
                chatListItems.add(chatListItem);

            }
        }
        chatListAdapter.notifyDataSetChanged();
        ibtnChat.callOnClick();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // 롱클릭했을 때 나오는 context Menu 의 항목을 선택(클릭) 했을 때 호출
        switch(item.getItemId()) {
            case 1 :// 빨강 메뉴 선택시

                return true;
            case 2 :// 녹색 메뉴 선택시

                return true;
            case 3 :// 파랑 메뉴 선택시

                return true;
        }

        return super.onContextItemSelected(item);
    }

    class FileNameSort implements Comparator<ChatListItem> {
        public int compare(ChatListItem f1, ChatListItem f2) {
            return f1.getDate() > f2.getDate() ? -1 : f1.getDate() < f2.getDate() ? 1:0;
        }
    }
}

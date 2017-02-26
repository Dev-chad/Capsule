package kr.co.teamnova.chad.capsule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private static final String TAG = "ChatActivity";

    private ListView listViewChat;
    private ChatAdapter adapter;
    private ArrayList<Chat> listChat;

    private String strLoginUserTotalMessage;
    private String strChatUserTotalMessage;

    private SharedPreferences.Editor spMessageEditor;
    private long beforeTime = 0;
    private long currentTime;

    SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
    SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH시 mm분", Locale.KOREA);
    SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
    SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
    SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
    SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH", Locale.KOREA);
    SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm", Locale.KOREA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final User loginUser = getIntent().getParcelableExtra("login_user");
        final User chatUser = getIntent().getParcelableExtra("chat_user");

        setTitle(chatUser.getNickname());

        final EditText editChat = (EditText) findViewById(R.id.edit_chat);
        ImageButton ibtnChat = (ImageButton) findViewById(R.id.ibtn_chat);
        listViewChat = (ListView) findViewById(R.id.listView_chat);

        SharedPreferences spMessage = getSharedPreferences("message", MODE_PRIVATE);
        spMessageEditor = spMessage.edit();

        listChat = new ArrayList<>();

        strLoginUserTotalMessage = spMessage.getString(loginUser.getEmail() + "," + chatUser.getEmail(), "");
        strChatUserTotalMessage = spMessage.getString(chatUser.getEmail() + "," + loginUser.getEmail(), "");

        if (!strLoginUserTotalMessage.equals("")) {
            beforeTime = Long.valueOf(strLoginUserTotalMessage.split(",")[0].split("::")[2]);
            listChat.add(new Chat(CurDateFormat.format(beforeTime)));

            for (String strMessage : strLoginUserTotalMessage.split(",")) {
                String[] strMessageDetail = strMessage.split("::");

                currentTime = Long.valueOf(strMessageDetail[2]);

                if (!CurDateFormat.format(beforeTime).equals(CurDateFormat.format(currentTime))) {
                    listChat.add(new Chat(CurDateFormat.format(currentTime)));
                }

                Chat chat = new Chat(strMessageDetail[0], Utils.getStringFromByteString(strMessageDetail[1], "\\+"), CurTimeFormat.format(currentTime));
                listChat.add(chat);
                beforeTime = currentTime;
            }
        }

        adapter = new ChatAdapter(loginUser.getNickname(), chatUser.getNickname(), chatUser.getUriProfileImage(), listChat);
        listViewChat.setAdapter(adapter);
        if (listChat.size() > 1) {
            listViewChat.setSelection(listChat.size() - 1);
        }

        ibtnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = Utils.getByteStringForm(editChat.getText().toString(), "+");
                currentTime = System.currentTimeMillis();
                if (strLoginUserTotalMessage.equals("")) {
                    strLoginUserTotalMessage = loginUser.getNickname() + "::" + message + "::" + currentTime;
                } else {
                    strLoginUserTotalMessage += ("," + loginUser.getNickname() + "::" + message + "::" + currentTime);
                }

                if(strChatUserTotalMessage.equals("")){
                    strChatUserTotalMessage = loginUser.getNickname() + "::" + message + "::" + currentTime;
                } else {
                    strChatUserTotalMessage += ("," + loginUser.getNickname() + "::" + message + "::" + currentTime);
                }

                spMessageEditor.putString(loginUser.getEmail()+","+chatUser.getEmail(), strLoginUserTotalMessage);
                spMessageEditor.putString(chatUser.getEmail()+","+loginUser.getEmail(), strChatUserTotalMessage);
                spMessageEditor.apply();

                if(listChat.size() == 0){
                    listChat.add(new Chat(CurDateFormat.format(currentTime)));
                } else if(!CurDateFormat.format(beforeTime).equals(CurDateFormat.format(currentTime))){
                    listChat.add(new Chat(CurDateFormat.format(currentTime)));
                }

                Chat chat = new Chat(loginUser.getNickname(), editChat.getText().toString(), CurTimeFormat.format(currentTime));
                listChat.add(chat);

                adapter.notifyDataSetChanged();
                listViewChat.setSelection(listChat.size() - 1);

                beforeTime = currentTime;
                editChat.setText("");
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
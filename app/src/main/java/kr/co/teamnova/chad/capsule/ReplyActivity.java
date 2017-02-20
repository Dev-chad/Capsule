package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class ReplyActivity extends AppCompatActivity {

    private static final String TAG = "ReplyActivity";
    private String[] strCurrentContentArray = {};
    String[] strTotalContentArray = {};
    private int position = 0;
    private SharedPreferences spContent;
    private SharedPreferences.Editor spContentEditor;
    private Content content;

    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final User loginUser = getIntent().getParcelableExtra("login_user");
        content = getIntent().getParcelableExtra("content");


        ListView listViewReply = (ListView) findViewById(R.id.listView_reply);

        ArrayList<Reply> replyList = content.getReplyList();

        final ReplyListAdapter adapter = new ReplyListAdapter(loginUser, replyList);
        listViewReply.setAdapter(adapter);

        final EditText editReply = (EditText) findViewById(R.id.edit_reply);
        ImageButton ibtnAddReply = (ImageButton) findViewById(R.id.ibtn_reply);

        spContent = getSharedPreferences("contents", MODE_PRIVATE);
        spContentEditor = spContent.edit();

        strTotalContentArray = spContent.getString(content.getPublisherEmail(), "").split(",");

        for (String strFindContent : strTotalContentArray) {
            String[] strFindContentDetail = strFindContent.split("::");
            if (strFindContentDetail[Const.CONTENT_TIME].equals(String.valueOf(content.getDateToMillisecond()))) {
                strCurrentContentArray = strFindContentDetail;
                break;
            }
            position++;
        }

        ibtnAddReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strReply = strCurrentContentArray[Const.CONTENT_REPLY];
                if (strReply.equals(" ")) {
                    strReply = loginUser.getEmail() + "/" + Utils.getByteStringForm(editReply.getText().toString(), "#") + "/" + System.currentTimeMillis() + "/" + " ";
                } else {
                    strReply += ("+" + loginUser.getEmail() + "/" + Utils.getByteStringForm(editReply.getText().toString(), "#") + "/" + System.currentTimeMillis() + "/" + " ");
                }

                strCurrentContentArray[Const.CONTENT_REPLY] = strReply;
                String strUpdatedCurrentContent = strCurrentContentArray[0];
                for (int i = 1; i < strCurrentContentArray.length; i++) {
                    strUpdatedCurrentContent += ("::" + strCurrentContentArray[i]);
                }

                strTotalContentArray[position] = strUpdatedCurrentContent;
                String updatedTotalContent = strTotalContentArray[0];
                for (int i = 1; i < strTotalContentArray.length; i++) {
                    updatedTotalContent += ("," + strTotalContentArray[i]);
                }

                Log.d(TAG, updatedTotalContent);
                spContentEditor.putString(content.getPublisherEmail(), updatedTotalContent);
                spContentEditor.apply();

                Reply reply = new Reply(loginUser, editReply.getText().toString(), System.currentTimeMillis());
                adapter.addReply(reply);

                isChanged = true;
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isChanged) {
            Intent intent = new Intent();
            intent.putExtra("content", content);
            intent.putExtra("position", getIntent().getIntExtra("position", 0));
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, null);
        }
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (isChanged) {
            Intent intent = new Intent();
            intent.putExtra("content", content);
            intent.putExtra("position", getIntent().getIntExtra("position", 0));
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, null);
        }

        finish();
    }
}

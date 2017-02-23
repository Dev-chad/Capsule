package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-23.
 */

public class Reply2Activity extends AppCompatActivity {

    private static final String TAG = "Reply2Activity";
    private boolean isChanged = false;
    private ArrayList<Reply> replyList;
    private ReplyList2Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final User loginUser = getIntent().getParcelableExtra("login_user");
        final Content content = getIntent().getParcelableExtra("content");
        final Reply upperReply = getIntent().getParcelableExtra("reply");
        replyList = upperReply.getReplyList();

        ListView listView = (ListView)findViewById(R.id.listView_reply);
        adapter = new ReplyList2Adapter(this, loginUser, replyList, content, upperReply);
        listView.setAdapter(adapter);

        final EditText editReply = (EditText) findViewById(R.id.edit_reply);
        ImageButton ibtnAddReply = (ImageButton) findViewById(R.id.ibtn_reply);

        ibtnAddReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime = System.currentTimeMillis();
                String strReply = editReply.getText().toString();
                Reply reply = new Reply(loginUser, strReply, currentTime);
                adapter.addReply(reply);

                SharedPreferences spContent = getSharedPreferences("contents", MODE_PRIVATE);
                SharedPreferences.Editor spContentEditor = spContent.edit();

                int contentPosition = 0;

                String[] strTotalContent = spContent.getString(content.getPublisherEmail(), "").split(",");
                String[] strContentDetail = {};

                for(String findContent : strTotalContent){
                    String[] findContentDetails = findContent.split("::");

                    if(findContentDetails[Const.CONTENT_TIME].equals(String.valueOf(content.getDateToMillisecond()))){
                        strContentDetail = findContentDetails;
                        Log.d(TAG, "Content:  " + findContent);
                        break;
                    }

                    contentPosition++;
                }

                String[] strTotalReply = strContentDetail[Const.CONTENT_REPLY].split("\\+");
                String[] strReplyDetail = {};
                int replyPosition = 0;

                for(String findReply : strTotalReply){
                    String[] findReplyDetails = findReply.split("/");
                    if(findReplyDetails[Const.REPLY_TIME].equals(String.valueOf(upperReply.getDateMilliSec()))){
                        strReplyDetail = findReplyDetails;
                        Log.d(TAG, "Reply:  " + findReply);
                        break;
                    }
                    replyPosition++;
                }

                String[] strTotalReply2 = strReplyDetail[Const.REPLY_REPLY].split("#");
                if(strTotalReply2[0].equals(" ")){
                    strReplyDetail[Const.REPLY_REPLY] = loginUser.getEmail() + "&" + Utils.getByteStringForm(strReply, "*") + "&" + currentTime;
                }else {
                    strReplyDetail[Const.REPLY_REPLY] += ("#"+loginUser.getEmail() + "&" + Utils.getByteStringForm(strReply, "*") + "&" + currentTime);
                }

                String updatedReply = strReplyDetail[0];
                for(int i=1; i<strReplyDetail.length; i++){
                    updatedReply += ("/"+strReplyDetail[i]);
                }

                strTotalReply[replyPosition] = updatedReply;
                String updatedTotalReply = strTotalReply[0];
                for(int i=1; i<strTotalReply.length; i++){
                    updatedTotalReply += ("+"+strTotalReply[i]);
                }
                Log.d(TAG, "TotalReply:  " + updatedTotalReply);


                strContentDetail[Const.CONTENT_REPLY] = updatedTotalReply;
                String updatedContent = strContentDetail[0];
                for(int i=1; i<strContentDetail.length; i++){
                    updatedContent += ("::"+strContentDetail[i]);
                }
                Log.d(TAG, "content:  " + updatedContent);


                strTotalContent[contentPosition] = updatedContent;
                String updatedTotalContent = strTotalContent[0];
                for(int i=1; i<strTotalContent.length; i++){
                    updatedTotalContent += (","+strTotalContent[i]);
                }

                Log.d(TAG, "Total:  " + updatedTotalContent);

                spContentEditor.putString(content.getPublisherEmail(), updatedTotalContent);
                spContentEditor.apply();
                editReply.setText("");
                isChanged = true;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editReply.getWindowToken(), 0);
            }
        });

    }

    public void setChanged(boolean isChanged){
        this.isChanged = isChanged;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(isChanged){
            Intent intent = getIntent();
            intent.putParcelableArrayListExtra("reply_list", replyList);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, null);
        }
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(isChanged){
            Intent intent = getIntent();
            intent.putParcelableArrayListExtra("reply_list", replyList);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, null);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            replyList.get(data.getIntExtra("position", 0)).setDesc(data.getStringExtra("changed"));
            adapter.notifyDataSetChanged();
            isChanged = true;
        }
    }
}

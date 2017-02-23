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

public class ReplyActivity extends AppCompatActivity {

    private static final String TAG = "ReplyActivity";
    private String[] strCurrentContentArray = {};
    String[] strTotalContentArray = {};
    private int position = 0;
    private SharedPreferences spContent;
    private SharedPreferences.Editor spContentEditor;
    private Content content;

    private EditText editReply;

    private ReplyListAdapter adapter;

    private Reply editTargetReply;
    private int editReplyPosition;

    private boolean isChanged = false;
    private boolean isEdited = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final User loginUser = getIntent().getParcelableExtra("login_user");
        content = getIntent().getParcelableExtra("content");

        final ListView listViewReply = (ListView) findViewById(R.id.listView_reply);

        adapter = new ReplyListAdapter(this, loginUser, content);
        listViewReply.setAdapter(adapter);

        editReply = (EditText) findViewById(R.id.edit_reply);
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
                if(isEdited){
                    String[] strReply = strCurrentContentArray[Const.CONTENT_REPLY].split("\\+");
                    Long replyTime = editTargetReply.getDateMilliSec();

                    String updatedReply = "";
                    for(String strFindReply : strReply){
                        String[] strFindReplyDetail = strFindReply.split("/");
                        if(strFindReplyDetail[Const.REPLY_TIME].equals(String.valueOf(replyTime))){
                            strFindReplyDetail[Const.REPLY_DECS] = Utils.getByteStringForm(editReply.getText().toString(), "#");

                            strFindReply = strFindReplyDetail[0];
                            for(int i=1; i<strFindReplyDetail.length; i++){
                                strFindReply += ("/" + strFindReplyDetail[i]);
                            }
                        }

                        if(updatedReply.equals("")){
                            updatedReply = strFindReply;
                        } else {
                            updatedReply += ("+" + strFindReply);
                        }
                    }

                    strCurrentContentArray[Const.CONTENT_REPLY] = updatedReply;
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

                    Reply editedReply = (Reply)adapter.getItem(editReplyPosition);
                    editedReply.setDesc(editReply.getText().toString());
                    adapter.setEditPosition(-1);
                    adapter.notifyDataSetChanged();
                }else {
                    String strReply = strCurrentContentArray[Const.CONTENT_REPLY];
                    Long currentTime = System.currentTimeMillis();
                    if (strReply.equals(" ")) {
                        strReply = loginUser.getEmail() + "/" + Utils.getByteStringForm(editReply.getText().toString(), "#") + "/" + currentTime + "/" + " ";
                    } else {
                        strReply += ("+" + loginUser.getEmail() + "/" + Utils.getByteStringForm(editReply.getText().toString(), "#") + "/" + currentTime + "/" + " ");
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

                    Reply reply = new Reply(loginUser, editReply.getText().toString(), currentTime);
                    adapter.addReply(reply);
                }
                isChanged = true;
                if(isEdited){
                    isEdited =false;
                } else {
                    listViewReply.setSelection(adapter.getCount()-1);
                }
                editReply.setText("");

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editReply.getWindowToken(), 0);


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
        if(isEdited){
            isEdited = false;
            editReply.setText("");
            adapter.setEditPosition(-1);
            adapter.notifyDataSetChanged();
        } else {
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

    public void setChanged(boolean isChanged){
        this.isChanged = isChanged;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 0){
                isChanged = true;
                int position = data.getIntExtra("position", 0);
                ArrayList<Reply> reply = data.getParcelableArrayListExtra("reply_list");
                content.getReplyList().get(position).setReplyList(reply);
                adapter.notifyDataSetChanged();
            } else {

            }
        }
    }
}

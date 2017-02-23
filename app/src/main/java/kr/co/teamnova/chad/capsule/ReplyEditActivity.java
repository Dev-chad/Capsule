package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ReplyEditActivity extends AppCompatActivity {

    String[] strCurrentContentArray;
    String[] strTotalContentArray = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_edit);

        final String mode = getIntent().getStringExtra("mode");
        setTitle(mode + " 수정");

        final Content content = getIntent().getParcelableExtra("content");
        final Reply upperReply = getIntent().getParcelableExtra("upper_reply");
        final Reply reply = getIntent().getParcelableExtra("reply");

        ImageView imageProfile = (ImageView)findViewById(R.id.image_profile);
        final EditText editReply = (EditText)findViewById(R.id.edit_reply);
        TextView textNickname = (TextView)findViewById(R.id.text_nickname);

        imageProfile.setImageURI(reply.getUser().getUriProfileImage());
        textNickname.setText(reply.getUser().getNickname());
        editReply.setText(reply.getDesc());

        Button btnOk = (Button)findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spContent = getSharedPreferences("contents", MODE_PRIVATE);
                SharedPreferences.Editor spContentEditor = spContent.edit();

                strTotalContentArray = spContent.getString(content.getPublisherEmail(), "").split(",");

                int contentPos = 0;
                for (String strFindContent : strTotalContentArray) {
                    String[] strFindContentDetail = strFindContent.split("::");
                    if (strFindContentDetail[Const.CONTENT_TIME].equals(String.valueOf(content.getDateToMillisecond()))) {
                        strCurrentContentArray = strFindContentDetail;
                        break;
                    }
                    contentPos++;
                }
                String[] strReply = strCurrentContentArray[Const.CONTENT_REPLY].split("\\+");
                String updatedReply = "";

                if(mode.equals("댓글")){
                    for (String strFindReply : strReply) {
                        String[] strFindReplyDetail = strFindReply.split("/");
                        if (strFindReplyDetail[Const.REPLY_TIME].equals(String.valueOf(reply.getDateMilliSec()))) {
                            strFindReplyDetail[Const.REPLY_DECS] = Utils.getByteStringForm(editReply.getText().toString(), "#");

                            strFindReply = strFindReplyDetail[0];
                            for (int i = 1; i < strFindReplyDetail.length; i++) {
                                strFindReply += ("/" + strFindReplyDetail[i]);
                            }
                        }

                        if (updatedReply.equals("")) {
                            updatedReply = strFindReply;
                        } else {
                            updatedReply += ("+" + strFindReply);
                        }
                    }
                } else {
                    String[] strUpperReply = {};
                    int upperReplyPos = 0;
                    for (String strFindReply : strReply) {
                        String[] strFindReplyDetail = strFindReply.split("/");
                        if (strFindReplyDetail[Const.REPLY_TIME].equals(String.valueOf(upperReply.getDateMilliSec()))) {
                            strUpperReply = strFindReplyDetail;
                            break;
                        }
                        upperReplyPos++;
                    }

                    String[] strTotalInnerReply = strUpperReply[Const.REPLY_REPLY].split("#");
                    String updatedInnerReply = "";
                    for(String strFindInnerReply:strTotalInnerReply){
                        String[] strInnerReplyDetail = strFindInnerReply.split("&");
                        if(strInnerReplyDetail[Const.REPLY_TIME].equals(String.valueOf(reply.getDateMilliSec()))){
                            strInnerReplyDetail[Const.REPLY_DECS] = Utils.getByteStringForm(editReply.getText().toString(), "*");

                            strFindInnerReply = strInnerReplyDetail[0];
                            for(int i=1; i<strInnerReplyDetail.length; i++){
                                strFindInnerReply += ("&" + strInnerReplyDetail[i]);
                            }
                        }

                        if(updatedInnerReply.equals("")){
                            updatedInnerReply = strFindInnerReply;
                        } else {
                            updatedInnerReply += ("#" + strFindInnerReply);
                        }
                    }

                    strUpperReply[Const.REPLY_REPLY] = updatedInnerReply;
                    String updatedUpperReply = strUpperReply[0];
                    for(int i=1; i<strUpperReply.length; i++){
                        updatedUpperReply += ("/"+strUpperReply[i]);
                    }

                    strReply[upperReplyPos] = updatedUpperReply;
                    updatedReply = strReply[0];
                    for(int i=1; i<strReply.length; i++){
                        updatedReply += ("+" + strReply[i]);
                    }
                }

                strCurrentContentArray[Const.CONTENT_REPLY] = updatedReply;
                String strUpdatedCurrentContent = strCurrentContentArray[0];
                for (int i = 1; i < strCurrentContentArray.length; i++) {
                    strUpdatedCurrentContent += ("::" + strCurrentContentArray[i]);
                }

                strTotalContentArray[contentPos] = strUpdatedCurrentContent;
                String updatedTotalContent = strTotalContentArray[0];
                for (int i = 1; i < strTotalContentArray.length; i++) {
                    updatedTotalContent += ("," + strTotalContentArray[i]);
                }

                spContentEditor.putString(content.getPublisherEmail(), updatedTotalContent);
                spContentEditor.apply();

                Intent intent = getIntent();
                intent.putExtra("changed", editReply.getText().toString());
                setResult(RESULT_OK, intent);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editReply.getWindowToken(), 0);

                finish();
            }
        });

        Button btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
    }
}

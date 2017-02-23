package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-21.
 */

public class ReplyList2Adapter extends BaseAdapter {
    private static final String TAG = "ReplyList2Adapter";

    public class ViewHolder{
        ImageView imageProfile;
        ImageView imageProfileSmall;
        TextView textNickname;
        TextView textReply;
        TextView textTime;
        ImageButton ibtnMenu;
    }

    private Reply2Activity activity;
    private ViewHolder viewHolder;
    private ArrayList<Reply> replyList;
    private User loginUser;
    private Content content;
    private Reply upperReply;

    public ReplyList2Adapter(Reply2Activity activity, User loginUser, ArrayList<Reply> replyList, Content content, Reply upperReply){
        this.loginUser = loginUser;
        this.replyList = replyList;
        this.activity = activity;
        this.content = content;
        this.upperReply = upperReply;
    }

    @Override
    public int getCount() {
        return replyList.size();
    }

    @Override
    public Object getItem(int position) {
        return replyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Context context = parent.getContext();
        final Reply reply = replyList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_reply2, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageProfile = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.imageProfileSmall = (ImageView) convertView.findViewById(R.id.image_profile_reply);
            viewHolder.textNickname = (TextView)convertView.findViewById(R.id.text_nickname);
            viewHolder.textReply = (TextView)convertView.findViewById(R.id.text_reply);
            viewHolder.textTime = (TextView)convertView.findViewById(R.id.text_time);
            viewHolder.ibtnMenu = (ImageButton)convertView.findViewById(R.id.ibtn_menu);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageProfile.setImageURI(reply.getUser().getUriProfileImage());
        viewHolder.textNickname.setText(reply.getUser().getNickname());
        viewHolder.textReply.setText(reply.getDesc());
        viewHolder.textTime.setText(Utils.getTime(reply.getDateMilliSec()));

        if(reply.getUser().getNickname().equals(loginUser.getNickname())){
            viewHolder.ibtnMenu.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ibtnMenu.setVisibility(View.GONE);
        }

        viewHolder.ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p = new PopupMenu(parent.getContext(), v);
                p.getMenuInflater().inflate(R.menu.popup_menu_listview, p.getMenu());
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_edit:
                                Log.d(TAG, "edit");
                                Intent intent = new Intent(context, ReplyEditActivity.class);
                                intent.putExtra("content", content);
                                intent.putExtra("upper_reply", upperReply);
                                intent.putExtra("reply", reply);
                                intent.putExtra("position", position);
                                intent.putExtra("mode", "답글");
                                activity.startActivityForResult(intent, 0);
                                break;

                            case R.id.menu_delete:
                                Log.d(TAG, "delete");
                                SharedPreferences spContents = context.getSharedPreferences("contents", Context.MODE_PRIVATE);
                                SharedPreferences.Editor spContentsEditor = spContents.edit();

                                String[] strTotalContents = spContents.getString(content.getPublisherEmail(), "").split(",");
                                String[] strContentsDetail = {};
                                int contentPosition = 0;

                                Log.d(TAG, spContents.getString(content.getPublisherEmail(), ""));

                                for(String strContentFind : strTotalContents){
                                    String[] strContentFindDetail = strContentFind.split("::");
                                    if(strContentFindDetail[Const.CONTENT_TIME].equals(String.valueOf(content.getDateToMillisecond()))){
                                        strContentsDetail = strContentFindDetail;
                                        break;
                                    }
                                    contentPosition++;
                                }

                                String[] strTotalReply = strContentsDetail[Const.CONTENT_REPLY].split("\\+");
                                String[] strCurrentReply = {};
                                int replyPosition = 0;

                                for(String strReply : strTotalReply){
                                    String[] strReplyDetail = strReply.split("/");
                                    if(strReplyDetail[Const.REPLY_TIME].equals(String.valueOf(upperReply.getDateMilliSec()))){
                                        strCurrentReply = strReplyDetail;
                                        Log.d(TAG, strReply + ",  " + upperReply.getDateMilliSec());
                                        break;
                                    }
                                    replyPosition++;
                                }


                                String[] strTotalInnerReply = strCurrentReply[Const.REPLY_REPLY].split("#");
                                String updatedInnerReply = "";

                                for(String strInnerReply : strTotalInnerReply){
                                    String[] strInnerReplyDetail = strInnerReply.split("&");

                                    if(!strInnerReplyDetail[Const.REPLY_TIME].equals(String.valueOf(reply.getDateMilliSec()))){
                                        if(updatedInnerReply.equals("")){
                                            updatedInnerReply = strInnerReply;
                                        } else {
                                            updatedInnerReply += ("#" + strInnerReply);
                                        }
                                    }
                                }

                                if(updatedInnerReply.equals("")){
                                    updatedInnerReply = " ";
                                }


                                strCurrentReply[Const.REPLY_REPLY] = updatedInnerReply;
                                String updatedCurrentReply = strCurrentReply[0];
                                for(int i=1; i<strCurrentReply.length; i++){
                                    updatedCurrentReply += ("/"+strCurrentReply[i]);
                                }

                                strTotalReply[replyPosition] = updatedCurrentReply;
                                String updatedTotalReply = strTotalReply[0];
                                for(int i=1; i<strTotalReply.length; i++){
                                    updatedTotalReply += ("+"+strTotalReply[i]);
                                }

                                strContentsDetail[Const.CONTENT_REPLY] = updatedTotalReply;
                                String updatedCurrentContent = strContentsDetail[0];
                                for(int i=1; i<strContentsDetail.length; i++){
                                    updatedCurrentContent += ("::"+strContentsDetail[i]);
                                }

                                strTotalContents[contentPosition] = updatedCurrentContent;
                                String updatedTotalContents = strTotalContents[0];
                                for(int i=1; i<strTotalContents.length; i++){
                                    updatedTotalContents += (","+strTotalContents[i]);
                                }

                                spContentsEditor.putString(content.getPublisherEmail(), updatedTotalContents);
                                spContentsEditor.apply();
                                Log.d(TAG, updatedTotalContents);
                                replyList.remove(position);
                                activity.setChanged(true);
                                notifyDataSetChanged();
                                break;

                            default:
                                return false;
                        }
                        return true;
                    }
                });
                p.show();
            }
        });

        return convertView;
    }

    public void addReply(Reply reply){
        replyList.add(reply);
        notifyDataSetChanged();
    }
}

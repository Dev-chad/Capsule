package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-21.
 */

public class ReplyListAdapter extends BaseAdapter {

    public class ViewHolder{
        ImageView imageProfile;
        ImageView imageProfileSmall;
        TextView textNickname;
        TextView textReply;
        TextView textTime;
        TextView textAddReply;
        TextView textViewBeforeReply;
        TextView textLastReply;
        RelativeLayout layoutReplySmall;
        ImageButton ibtnMenu;
    }

    private ViewHolder viewHolder;
    private ArrayList<Reply> replyList;
    private User loginUser;

    public ReplyListAdapter(User loginUser, ArrayList<Reply> replyList){
        this.loginUser = loginUser;
        this.replyList = replyList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        Reply reply = replyList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_reply, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageProfile = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.imageProfileSmall = (ImageView) convertView.findViewById(R.id.image_profile_reply);
            viewHolder.textNickname = (TextView)convertView.findViewById(R.id.text_nickname);
            viewHolder.textReply = (TextView)convertView.findViewById(R.id.text_reply);
            viewHolder.textTime = (TextView)convertView.findViewById(R.id.text_time);
            viewHolder.textAddReply = (TextView)convertView.findViewById(R.id.text_add_reply);
            viewHolder.textLastReply = (TextView)convertView.findViewById(R.id.text_last_reply);
            viewHolder.textViewBeforeReply = (TextView)convertView.findViewById(R.id.text_reply_before);
            viewHolder.layoutReplySmall = (RelativeLayout)convertView.findViewById(R.id.layout_reply_small);
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

        if(reply.getReplyList().size() > 0 ){
            viewHolder.layoutReplySmall.setVisibility(View.VISIBLE);
            if(reply.getReplyList().size() > 1){
                viewHolder.textViewBeforeReply.setVisibility(View.VISIBLE);
            } else{
                viewHolder.textViewBeforeReply.setVisibility(View.GONE);
            }
        } else {
            viewHolder.layoutReplySmall.setVisibility(View.GONE);
            viewHolder.textViewBeforeReply.setVisibility(View.GONE);
        }

        viewHolder.textAddReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public void addReply(Reply reply){
        replyList.add(reply);
        notifyDataSetChanged();
    }
}

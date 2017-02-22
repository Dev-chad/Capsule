package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-21.
 */

public class ReplyList2Adapter extends BaseAdapter {

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

    public ReplyList2Adapter(Reply2Activity activity, User loginUser, ArrayList<Reply> replyList){
        this.loginUser = loginUser;
        this.replyList = replyList;
        this.activity = activity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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

            }
        });

        return convertView;
    }

    public void addReply(Reply reply){
        replyList.add(reply);
        notifyDataSetChanged();
    }
}

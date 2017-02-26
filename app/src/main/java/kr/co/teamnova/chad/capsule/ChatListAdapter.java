package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Chad on 2017-02-26.
 */

public class ChatListAdapter extends BaseAdapter {

    private ArrayList<ChatListItem> chatListItems;
    private ViewHolder viewHolder;

    public class ViewHolder{
        public ImageView imageProfile;
        public TextView textNickname;
        public TextView textLastChat;
        public TextView textDate;
    }

    public ChatListAdapter(ArrayList<ChatListItem> chatListItems) {
        this.chatListItems = chatListItems;
    }

    @Override
    public int getCount() {
        return chatListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return chatListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        ChatListItem chatListItem = chatListItems.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_chatlist, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageProfile = (ImageView)convertView.findViewById(R.id.image_profile);
            viewHolder.textNickname = (TextView)convertView.findViewById(R.id.text_nickname);
            viewHolder.textLastChat = (TextView)convertView.findViewById(R.id.text_chat);
            viewHolder.textDate = (TextView)convertView.findViewById(R.id.text_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageProfile.setImageURI(chatListItem.getUriProfileImage());
        viewHolder.textNickname.setText(chatListItem.getNickName());
        viewHolder.textLastChat.setText(chatListItem.getLastChat());
        long currentTime = System.currentTimeMillis();

        if(Utils.CurDateFormat.format(chatListItem.getDate()).equals(Utils.CurDateFormat.format(currentTime))){
            viewHolder.textDate.setText(Utils.CurTimeFormat.format(currentTime));
        }

        return convertView;
    }
}

package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-26.
 */

public class ChatAdapter extends BaseAdapter {
    private static final String TAG = "ChatAdapter";

    private String strLoginUserNickname;
    private String strSomeoneNickname;
    private Uri uriProfileImage;

    private ArrayList<Chat> chatList;
    private ViewHolder viewHolder;


    public class ViewHolder {

        public RelativeLayout layoutLoginUser;
        public TextView textLoginUserChat;
        public TextView textLoginUserTime;

        public RelativeLayout layoutSomeone;
        public ImageView imageProfile;
        public TextView textSomeoneChat;
        public TextView textSomeoneTime;
        public TextView textNickname;

        public LinearLayout layoutDate;
        public TextView textDate;
    }

    public ChatAdapter() {
    }

    public ChatAdapter(String strLoginUserNickname, String strSomeoneNickname, Uri uriProfileImage, ArrayList<Chat> chatList) {
        this.strLoginUserNickname = strLoginUserNickname;
        this.strSomeoneNickname = strSomeoneNickname;
        this.uriProfileImage = uriProfileImage;
        this.chatList = chatList;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        Chat chat = chatList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_chat, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.layoutLoginUser = (RelativeLayout) convertView.findViewById(R.id.layout_login_user);
            viewHolder.textLoginUserChat = (TextView) convertView.findViewById(R.id.text_login_user_chat);
            viewHolder.textLoginUserTime = (TextView) convertView.findViewById(R.id.text_login_user_time);

            viewHolder.layoutSomeone = (RelativeLayout) convertView.findViewById(R.id.layout_someone);
            viewHolder.imageProfile = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.imageProfile.setImageURI(uriProfileImage);
            viewHolder.textSomeoneChat = (TextView) convertView.findViewById(R.id.text_someone_chat);
            viewHolder.textSomeoneTime = (TextView) convertView.findViewById(R.id.text_someone_time);
            viewHolder.textNickname = (TextView) convertView.findViewById(R.id.text_nickname);
            viewHolder.textNickname.setText(strSomeoneNickname);

            viewHolder.layoutDate = (LinearLayout) convertView.findViewById(R.id.layout_date);
            viewHolder.textDate = (TextView) convertView.findViewById(R.id.text_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (chat.isTimeLine()) {
            viewHolder.layoutSomeone.setVisibility(View.GONE);
            viewHolder.layoutLoginUser.setVisibility(View.GONE);
            viewHolder.layoutDate.setVisibility(View.VISIBLE);

            viewHolder.textDate.setText(chat.getTime());
        } else {
            if (strLoginUserNickname.equals(chat.getStrNickname())) {
                viewHolder.layoutSomeone.setVisibility(View.GONE);
                viewHolder.layoutLoginUser.setVisibility(View.VISIBLE);
                viewHolder.layoutDate.setVisibility(View.GONE);

                viewHolder.textLoginUserChat.setText(chat.getStrMessage());

                if (position < chatList.size() - 1 && chatList.get(position+1).getStrNickname().equals(chat.getStrNickname()) && chatList.get(position + 1).getTime().equals(chat.getTime())) {
                    viewHolder.textLoginUserTime.setVisibility(View.GONE);
                } else {
                    viewHolder.textLoginUserTime.setVisibility(View.VISIBLE);
                    viewHolder.textLoginUserTime.setText(chat.getTime());
                }
            } else {
                viewHolder.layoutSomeone.setVisibility(View.VISIBLE);
                viewHolder.layoutLoginUser.setVisibility(View.GONE);
                viewHolder.layoutDate.setVisibility(View.GONE);

                viewHolder.textSomeoneChat.setText(chat.getStrMessage());
                if (chatList.get(position - 1).getStrNickname().equals(strSomeoneNickname) && chatList.get(position - 1).getTime().equals(chat.getTime())) {
                    viewHolder.imageProfile.setVisibility(View.GONE);
                    viewHolder.textNickname.setVisibility(View.GONE);
                } else {
                    viewHolder.imageProfile.setVisibility(View.VISIBLE);
                    viewHolder.textNickname.setVisibility(View.VISIBLE);
                }


                if (position < chatList.size() - 1 && chatList.get(position+1).getStrNickname().equals(chat.getStrNickname()) && chatList.get(position + 1).getTime().equals(chat.getTime())) {
                    viewHolder.textSomeoneTime.setVisibility(View.GONE);
                } else {
                    viewHolder.textSomeoneTime.setVisibility(View.VISIBLE);
                    viewHolder.textSomeoneTime.setText(chat.getTime());
                }
            }
        }


        return convertView;
    }

    public void addChat(Chat chat) {
        chatList.add(chat);
    }
}

package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-25.
 */

public class FollowListAdapter extends BaseAdapter{
    private static final String TAG = "FollowListAdapter";

    private ArrayList<User> followList = new ArrayList<>();
    private ViewHolder viewHolder;
    private int selectedItem = -1;
    private MessageActivity activity;
    private Animation inAnimation;
    private User loginUser;

    public FollowListAdapter(MessageActivity activity, User loginUser, ArrayList<User> followList){
        this.activity = activity;
        this.followList = followList;
        inAnimation = AnimationUtils.loadAnimation(activity, R.anim.translate_in_end);
        this.loginUser = loginUser;
    }

    public class ViewHolder {
        public ImageView imageViewProfile;
        public TextView textViewNickname;
        public TextView textViewEmail;
        public ImageButton ibtnAddChat;
    }


    @Override
    public int getCount() {
        return followList.size();
    }

    @Override
    public Object getItem(int position) {
        return followList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Context context = parent.getContext();

        User user = followList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_follow, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageViewProfile = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.textViewEmail = (TextView) convertView.findViewById(R.id.text_email);
            viewHolder.textViewNickname = (TextView) convertView.findViewById(R.id.text_nickname);
            viewHolder.ibtnAddChat = (ImageButton) convertView.findViewById(R.id.ibtn_add_chat);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(selectedItem == position){
            viewHolder.ibtnAddChat.setVisibility(View.VISIBLE);
            viewHolder.ibtnAddChat.startAnimation(inAnimation);
        } else {
            viewHolder.ibtnAddChat.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedItem == position){
                    selectedItem = -1;
                }
                selectedItem = position;
                notifyDataSetChanged();
            }
        });

        viewHolder.ibtnAddChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ChatActivity.class);
                intent.putExtra("login_user", loginUser);
                intent.putExtra("chat_user", followList.get(position));
                activity.startActivityForResult(intent, 0);
            }
        });

        viewHolder.imageViewProfile.setImageURI(user.getUriProfileImage());
        viewHolder.textViewEmail.setText(user.getEmail());
        viewHolder.textViewNickname.setText(user.getNickname());
        return convertView;
    }

    public void setSelectedItem(int position){
        selectedItem = position;
    }

}

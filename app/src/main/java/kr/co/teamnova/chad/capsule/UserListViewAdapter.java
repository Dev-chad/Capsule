package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-03.
 */

public class UserListViewAdapter extends BaseAdapter {
    private static final String TAG = "UserListViewAdapter";

    public class ViewHolder {
        public ImageView imageViewProfile;
        public TextView textViewNickname;
        public TextView textViewEmail;
        public Button btnFollow;
    }

    private User loginUser;
    private ViewHolder viewHolder;
    private UserListFragment fragment;
    private ArrayList<User> userList = new ArrayList<>();

    public UserListViewAdapter() {
    }

    public UserListViewAdapter(UserListFragment fragment, User loginUser) {
        this.fragment = fragment;
        this.loginUser = loginUser;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Context context = parent.getContext();

        final User user = userList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_user, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageViewProfile = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.textViewEmail = (TextView) convertView.findViewById(R.id.text_email);
            viewHolder.textViewNickname = (TextView) convertView.findViewById(R.id.text_nickname);
            viewHolder.btnFollow = (Button) convertView.findViewById(R.id.btn_follow);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageViewProfile.setImageURI(user.getUriProfileImage());
        viewHolder.textViewEmail.setText(user.getEmail());
        viewHolder.textViewNickname.setText(user.getNickname());

        if (loginUser.getFollowList().contains(user.getEmail())) {
            viewHolder.btnFollow.setText("Unfollow");
        } else {
            viewHolder.btnFollow.setText("Follow");
        }

        viewHolder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences spAccount = fragment.getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
                SharedPreferences.Editor spAccountEditor = spAccount.edit();

                String strLoginUserFollow = "";
                String strUserFollower = "";

                if (loginUser.getFollowList().contains(user.getEmail())) {
                    loginUser.getFollowList().remove(user.getEmail());
                    user.getFollowerList().remove(loginUser.getEmail());
                } else {
                    loginUser.getFollowList().add(user.getEmail());
                    user.getFollowerList().add(loginUser.getEmail());
                }

                for (String email : loginUser.getFollowList()) {
                    if (strLoginUserFollow.length() == 0) {
                        strLoginUserFollow = email;
                    } else {
                        strLoginUserFollow += ("::" + email);
                    }
                }

                for (String email : user.getFollowerList()) {
                    if (strUserFollower.length() == 0) {
                        strUserFollower = email;
                    } else {
                        strUserFollower += ("::" + email);
                    }
                }

                if(strLoginUserFollow.equals("")){
                    strLoginUserFollow = " ";
                }

                if(strUserFollower.equals("")){
                    strUserFollower = " ";
                }

                String[] strLoginUserData = spAccount.getString(loginUser.getEmail(), "").split(",");
                String[] strUserData = spAccount.getString(user.getEmail(), "").split(",");

                strLoginUserData[Const.INDEX_FOLLOW] = strLoginUserFollow;
                strUserData[Const.INDEX_FOLLOWER] = strUserFollower;

                String strUpdatedLoginUserData = strLoginUserData[0];
                String strUpdatedUserData = strUserData[0];

                for (int i = 1; i < strLoginUserData.length; i++) {
                    strUpdatedLoginUserData += ("," + strLoginUserData[i]);
                    strUpdatedUserData += ("," + strUserData[i]);
                }

                Log.d(TAG, "Login: " + strUpdatedLoginUserData);
                Log.d(TAG, "User: " + strUpdatedUserData);

                spAccountEditor.putString(loginUser.getEmail(), strUpdatedLoginUserData);
                spAccountEditor.putString(user.getEmail(), strUpdatedUserData);

                spAccountEditor.apply();
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void addUserList(ArrayList<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }
}

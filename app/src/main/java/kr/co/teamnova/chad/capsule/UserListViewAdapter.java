package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-03.
 */

public class UserListViewAdapter extends BaseAdapter {

    public class ViewHolder {
        public ImageView imageViewProfile;
        public TextView textViewNickname;
        public TextView textViewEmail;
        public Button btnFollow;
        public boolean isFollow;
    }

    private ViewHolder viewHolder;

    private ArrayList<ListViewUser> listViewUserList = new ArrayList<ListViewUser>();

    public UserListViewAdapter() {

    }

    @Override
    public int getCount() {
        return listViewUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Context context = parent.getContext();

        final ListViewUser listViewContent = listViewUserList.get(position);

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

        viewHolder.imageViewProfile.setImageURI(listViewContent.getProfileImageUri());
        viewHolder.textViewEmail.setText(listViewContent.getEmail());
        viewHolder.textViewNickname.setText(listViewContent.getNickname());
        if(listViewContent.isFollow()){
            Toast.makeText(context, "Call", Toast.LENGTH_SHORT).show();
            viewHolder.btnFollow.setText("Unfollow");
        } else {
            viewHolder.btnFollow.setText("Follow");
        }

        viewHolder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewContent.setFollow();
                Toast.makeText(context, "Follow Status: " + listViewContent.isFollow(), Toast.LENGTH_SHORT).show();
                if(!listViewContent.isFollow()){
                    viewHolder.btnFollow.setText("Follow");
                }else{
                    viewHolder.btnFollow.setText("Unfollow");
                }

            }
        });
        return convertView;
    }

    public void addItem(Uri uriProfileImage, String nickname, String email, boolean isFollow) {
        ListViewUser user = new ListViewUser();

        user.setEmail(email);
        user.setNickname(nickname);
        user.setProfileImageUri(uriProfileImage);
        user.setFollow(isFollow);
        listViewUserList.add(user);
    }
}

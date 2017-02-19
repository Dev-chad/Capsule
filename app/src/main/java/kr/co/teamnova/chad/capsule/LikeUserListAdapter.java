package kr.co.teamnova.chad.capsule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chad on 2017-02-19.
 */

public class LikeUserListAdapter extends BaseAdapter {

    public class ViewHolder {
        public ImageView imageViewProfile;
        public TextView textViewNickname;
        public TextView textViewEmail;
        public Button btnFollow;
    }

    private ArrayList<User> likeUserList = new ArrayList<>();
    private ViewHolder viewHolder;

    public LikeUserListAdapter() {

    }

    public LikeUserListAdapter(ArrayList<User> likeUserList) {
        this.likeUserList = likeUserList;
    }

    @Override
    public int getCount() {
        return likeUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return likeUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        User user = likeUserList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_user, parent, false);
            viewHolder = new LikeUserListAdapter.ViewHolder();

            viewHolder.imageViewProfile = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.textViewEmail = (TextView) convertView.findViewById(R.id.text_email);
            viewHolder.textViewNickname = (TextView) convertView.findViewById(R.id.text_nickname);
            viewHolder.btnFollow = (Button) convertView.findViewById(R.id.btn_follow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LikeUserListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.imageViewProfile.setImageURI(user.getUriProfileImage());
        viewHolder.textViewEmail.setText(user.getEmail());
        viewHolder.textViewNickname.setText(user.getNickname());
        viewHolder.btnFollow.setVisibility(View.GONE);
        return convertView;
    }
}

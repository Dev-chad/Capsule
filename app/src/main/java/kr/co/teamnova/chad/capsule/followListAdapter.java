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
 * Created by Chad on 2017-02-25.
 */

public class FollowListAdapter extends BaseAdapter {

    private ArrayList<User> followList = new ArrayList<>();
    private ViewHolder viewHolder;

    public FollowListAdapter(ArrayList<User> followList){
        this.followList = followList;
    }

    public class ViewHolder {
        public ImageView imageViewProfile;
        public TextView textViewNickname;
        public TextView textViewEmail;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        User user = followList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_follow, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageViewProfile = (ImageView) convertView.findViewById(R.id.image_profile);
            viewHolder.textViewEmail = (TextView) convertView.findViewById(R.id.text_email);
            viewHolder.textViewNickname = (TextView) convertView.findViewById(R.id.text_nickname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageViewProfile.setImageURI(user.getUriProfileImage());
        viewHolder.textViewEmail.setText(user.getEmail());
        viewHolder.textViewNickname.setText(user.getNickname());
        return convertView;
    }
}

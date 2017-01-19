package kr.co.teamnova.chad.capsule;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Chad on 2017-01-18.
 */

// TODO: Implementation activity view.
public class HomeFragment extends Fragment {

    private ImageView imagePorfile;
    private TextView textContentCount;
    private GridLayout layoutContents;
    private TextView textNothingContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences userData = this.getActivity().getSharedPreferences(getArguments().getString("email"), Context.MODE_PRIVATE);

        imagePorfile = (ImageView) view.findViewById(R.id.image_profile);

        if(userData.getString("profile_image", "").equals("")){
            imagePorfile.setImageResource(R.mipmap.ic_launcher);
        } else{
            imagePorfile.setImageURI(Uri.parse(userData.getString("profile_image", "")));
        }

        imagePorfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView textNickname;
        textNickname = (TextView) view.findViewById(R.id.text_nickname);
        textNickname.setText(userData.getString("nickname", ""));

        textNothingContent = (TextView) view.findViewById(R.id.text_nothing_content_const);

        layoutContents = (GridLayout) view.findViewById(R.id.layout_content);
        if(layoutContents.getChildCount() == 0){
            textNothingContent.setVisibility(View.VISIBLE);
        }

        textContentCount = (TextView) view.findViewById(R.id.text_content_count);
        textContentCount.setText(String.valueOf(layoutContents.getChildCount()));

        return view;
    }



}

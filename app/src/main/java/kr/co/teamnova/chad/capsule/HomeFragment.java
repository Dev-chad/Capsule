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
import android.widget.Toast;

import java.io.File;

/**
 * Created by Chad on 2017-01-18.
 */

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

    @Override
    public void onStart() {
        super.onStart();
        File test = new File("/data/data/" + getActivity().getPackageName() + "/User/" + getArguments().getString("email") + "/Content/1.txt");
        File test2 = new File("/data/data/" + getActivity().getPackageName() + "/User/" + getArguments().getString("email") + "/Content/1.jpg");

        Toast.makeText(getActivity().getApplicationContext(), test.getPath(), Toast.LENGTH_SHORT).show();
        if(test.exists()){
            Toast.makeText(getActivity().getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
        }
        if(test2.exists()){
            Toast.makeText(getActivity().getApplicationContext(), "test2", Toast.LENGTH_SHORT).show();
        }
    }
}

package kr.co.teamnova.chad.capsule;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chad on 2017-01-18.
 */

public class HomeFragment extends Fragment {

    private ImageView imagePorfile;
    private TextView textContentCount;
    private TextView textNothingContent;

    private ListView listViewContent;
    private ContentListViewAdapter adapter;

    private Uri uriProfileImage;
    private Uri uriContentImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences userData = getActivity().getSharedPreferences(getArguments().getString("email"), Context.MODE_PRIVATE);

        adapter = new ContentListViewAdapter();
        listViewContent = (ListView) view.findViewById(R.id.listView_content);
        listViewContent.setAdapter(adapter);

        textNothingContent = (TextView) view.findViewById(R.id.text_nothing_content_const);
        imagePorfile = (ImageView) view.findViewById(R.id.image_profile);
        if (userData.getString("profile_image", "").equals("")) {
            uriProfileImage = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.mipmap.ic_launcher);
            imagePorfile.setImageResource(R.mipmap.ic_launcher);
        } else {
            uriProfileImage = Uri.parse(userData.getString("profile_image", ""));
            imagePorfile.setImageURI(uriProfileImage);
        }

        textContentCount = (TextView) view.findViewById(R.id.text_content_count);

        // Read content description
        if (userData.getInt("num_of_content", -1) > 0) {
            textNothingContent.setVisibility(View.GONE);
            File[] file = new File("/data/data/" + getActivity().getPackageName() + "/User/" + getArguments().getString("email") + "/Contents").listFiles();
            if (file.length > 0) {
                for (int i=file.length-1; i>=0; i--) {
                    File contentDesc = file[i];
                    if (contentDesc.getName().contains(".txt")) {
                        Log.e("error", contentDesc.getPath());
                        long time = Long.parseLong(contentDesc.getName().split(".txt")[0]);
                        String strTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(time));
                        String strDesc = "";
                        try {
                            FileInputStream fis = new FileInputStream(contentDesc.getPath());
                            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                            String str;
                            if ((str = bufferReader.readLine()) != null) {
                                strDesc += str;
                                while ((str = bufferReader.readLine()) != null) {
                                    strDesc = strDesc + "\n" + str;
                                }
                            }
                            fis.close();
                            bufferReader.close();
                        } catch (Exception e) {
                            Log.e("error", e.toString());
                        }

                        File contentImage = new File("/data/data/" + getActivity().getPackageName() + "/User/" + getArguments().getString("email") + "/Contents/" + time + ".jpg");
                        if(contentImage.exists()){
                            uriContentImage = Uri.fromFile(contentImage);
                        } else {
                            uriContentImage = null;
                        }
                        adapter.addItem(uriContentImage, strDesc, uriProfileImage, userData.getString("nickname", ""), getArguments().getString("email"), strTime, String.valueOf(time));
                    }
                }
            }
        } else {
            textNothingContent.setVisibility(View.VISIBLE);
        }
        imagePorfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView textNickname;
        textNickname = (TextView) view.findViewById(R.id.text_nickname);
        textNickname.setText(userData.getString("nickname", ""));
        textContentCount.setText(String.valueOf(listViewContent.getAdapter().getCount()));
        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

    }
}
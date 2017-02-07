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
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Chad on 2017-01-18.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private ImageView imagePorfile;
    private TextView textContentCount;
    private TextView textNothingContent;

    private ListView listViewContent;
    private ContentListViewAdapter adapter;

    private Uri uriProfileImage;
    private Uri uriContentImage;

    private SharedPreferences userData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userData = getActivity().getSharedPreferences(getArguments().getString("email"), Context.MODE_PRIVATE);

        adapter = new ContentListViewAdapter(HomeFragment.this);
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

        if (userData.getInt("num_of_content", -1) > 0) {
            textNothingContent.setVisibility(View.GONE);
            File[] file = new File("/data/data/" + getActivity().getPackageName() + "/User/" + getArguments().getString("email") + "/Contents").listFiles();
            if (file.length > 0) {
                Arrays.sort(file, new FileNameSort());
                for(File contentDesc: file){
                    if (contentDesc.getName().contains(".txt")) {
                        long time = Long.parseLong(contentDesc.getName().split(".txt")[0]);
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
                            Log.e(TAG, e.toString());
                        }

                        File contentImage = new File("/data/data/" + getActivity().getPackageName() + "/User/" + getArguments().getString("email") + "/Contents/" + time + ".jpg");
                        if(contentImage.exists()){
                            uriContentImage = Uri.fromFile(contentImage);
                        } else {
                            uriContentImage = null;
                        }
                        adapter.addItem(uriContentImage, strDesc, uriProfileImage, userData.getString("nickname", ""), getArguments().getString("email"), time, String.valueOf(time));
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
        textContentCount.setText(""+userData.getInt("num_of_content", -1));
        return view;
    }

    public void updateContentCount(int position){
        textContentCount.setText(""+userData.getInt("num_of_content", -1));
        listViewContent.smoothScrollToPosition(position);
    }

    class FileNameSort implements Comparator<File> {
        public int compare(File f1, File f2) {
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    }
}
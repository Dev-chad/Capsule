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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chad on 2017-01-18.
 */

public class HomeFragment extends Fragment {

    public interface OnClickEditListener {
        public void EditClickEvent(ListViewContent origin);
    }

    private static final String TAG = "HomeFragment";

    SharedPreferences userData;
    private TextView textContentCount;
    private TextView textNothingContent;

    private ListView listViewContent;
    private ContentListViewAdapter adapter;

    private OnClickEditListener mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        User loginUser = getArguments().getParcelable("login_user");
        userData = getActivity().getSharedPreferences(loginUser.getEmail(), Context.MODE_PRIVATE);
        Set<String> followingSet = userData.getStringSet("following", new HashSet<String>());

        ArrayList<ListViewContent> totalContent = new ArrayList<>();

        adapter = new ContentListViewAdapter(HomeFragment.this, loginUser);
        listViewContent = (ListView) view.findViewById(R.id.listView_content);
        listViewContent.setAdapter(adapter);

        TextView textNickname = (TextView) view.findViewById(R.id.text_nickname);

        textContentCount = (TextView) view.findViewById(R.id.text_content_count);
        textNothingContent = (TextView) view.findViewById(R.id.text_nothing_content_const);
        ImageView imageProfile = (ImageView) view.findViewById(R.id.image_profile);
        imageProfile.setImageURI(loginUser.getUriProfileImage());

        for (String strFollow : followingSet) {
            SharedPreferences followUser = getActivity().getSharedPreferences(strFollow, Context.MODE_PRIVATE);

            File[] file = new File("/data/data/" + getActivity().getPackageName() + "/User/" + strFollow + "/Contents").listFiles();
            if (file.length > 0) {
                for (File contentDesc : file) {
                    if (contentDesc.getName().contains(".txt") && !contentDesc.getName().contains("_pref")) {
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

                        File contentImage = new File("/data/data/" + getActivity().getPackageName() + "/User/" + strFollow + "/Contents/" + time + ".jpg");
                        Uri uriContentImage;
                        if (contentImage.exists()) {
                            uriContentImage = Uri.fromFile(contentImage);
                        } else {
                            uriContentImage = null;
                        }

                        File contentPref = new File("/data/data/" + getActivity().getPackageName() + "/User/" + strFollow + "/Contents/" + time + "_pref.txt");
                        String location;
                        String strLocation = "";
                        if (contentPref.exists()) {
                            try {
                                FileInputStream fis = new FileInputStream(contentPref.getPath());
                                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                                if ((location = bufferReader.readLine()) != null) {
                                    strLocation = location.split(":")[1];
                                }
                                fis.close();
                                bufferReader.close();
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                        totalContent.add(new ListViewContent(uriContentImage, strDesc, Uri.parse(followUser.getString("profile_image", "")), followUser.getString("nickname", ""), strFollow, time, contentDesc.getName(), strLocation));
                    }
                }
            }
        }

        if (userData.getInt("num_of_content", 0) > 0) {
            textNothingContent.setVisibility(View.GONE);
            File[] file = new File("/data/data/" + getActivity().getPackageName() + "/User/" + loginUser.getEmail() + "/Contents").listFiles();
            if (file.length > 0) {
                for (File contentDesc : file) {
                    if (contentDesc.getName().contains(".txt") && !contentDesc.getName().contains("_pref")) {
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

                        File contentImage = new File("/data/data/" + getActivity().getPackageName() + "/User/" + loginUser.getEmail() + "/Contents/" + time + ".jpg");
                        Uri uriContentImage;
                        if (contentImage.exists()) {
                            uriContentImage = Uri.fromFile(contentImage);
                        } else {
                            uriContentImage = null;
                        }

                        File contentPref = new File("/data/data/" + getActivity().getPackageName() + "/User/" + loginUser.getEmail() + "/Contents/" + time + "_pref.txt");
                        String location;
                        String strLocation = "";
                        if (contentPref.exists()) {
                            try {
                                FileInputStream fis = new FileInputStream(contentPref.getPath());
                                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(fis));
                                if ((location = bufferReader.readLine()) != null) {
                                    strLocation = location.split(":")[1];
                                }
                                fis.close();
                                bufferReader.close();
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                        totalContent.add(new ListViewContent(uriContentImage, strDesc, loginUser.getUriProfileImage(), loginUser.getNickname(), loginUser.getEmail(), time, String.valueOf(time), strLocation));
                    }
                }
            }
        }

        if(totalContent.size() == 0){
            textNothingContent.setVisibility(View.VISIBLE);
        }
        Collections.sort(totalContent, new FileNameSort());
        Collections.reverse(totalContent);

        adapter.addItemFromArray(totalContent);

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        textNickname.setText(loginUser.getNickname());
        textContentCount.setText(String.valueOf(userData.getInt("num_of_content", 0)));
        return view;
    }

    public void updateContentCount(int position) {
        textContentCount.setText(String.valueOf(adapter.getCount()));
        adapter.notifyDataSetChanged();
        listViewContent.smoothScrollToPosition(position);
        if (adapter.getCount() == 0) {
            textNothingContent.setVisibility(View.VISIBLE);
        }
    }

    public void editContent(ListViewContent origin) {
        mCallback.EditClickEvent(origin);
    }

    class FileNameSort implements Comparator<ListViewContent> {
        public int compare(ListViewContent f1, ListViewContent f2) {
            return f1.getDateToMillisecond().compareTo(f2.getDateToMillisecond());
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (HomeFragment.OnClickEditListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnArticleSelectedListener");
        }
    }
}
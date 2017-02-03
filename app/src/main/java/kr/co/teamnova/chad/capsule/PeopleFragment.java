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
import android.widget.ListView;

import java.io.File;

/**
 * Created by Chad on 2017-01-18.
 */

public class PeopleFragment extends Fragment {

    ListView listViewUser;
    UserListViewAdapter adapter;

    public PeopleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);

        listViewUser = (ListView) view.findViewById(R.id.listView_user);
        adapter = new UserListViewAdapter();
        listViewUser.setAdapter(adapter);

        SharedPreferences spFollow = getActivity().getSharedPreferences("follow"+getArguments().getString("email"), Context.MODE_PRIVATE);
        SharedPreferences.Editor spFollowEditor = spFollow.edit();

        File[] file = new File("/data/data/" + getActivity().getPackageName() + "/shared_prefs").listFiles();

        for (File user : file) {
            if(user.getName().contains("@")){
                String email = user.getName().split(".xml")[0];
                if(!email.equals(getArguments().getString("email"))){
                    Log.v("email", email);
                    SharedPreferences sp = getActivity().getSharedPreferences(email, Context.MODE_PRIVATE);
                    Uri uriProfileImage;
                    if (sp.getString("profile_image", "").equals("")) {
                        uriProfileImage = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.mipmap.ic_launcher);
                    } else {
                        uriProfileImage = Uri.parse(sp.getString("profile_image", ""));
                    }
                    adapter.addItem(uriProfileImage, sp.getString("nickname", ""), email, spFollow.getBoolean(email, false));
                }
            }
        }

        return view;
    }
}

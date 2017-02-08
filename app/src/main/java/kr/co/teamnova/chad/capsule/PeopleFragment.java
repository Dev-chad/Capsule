package kr.co.teamnova.chad.capsule;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Chad on 2017-01-18.
 */

public class PeopleFragment extends Fragment {
    private static final String TAG = "PeopleFragment";

    ListView listViewUser;
    UserListViewAdapter adapter;

    public PeopleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);

        listViewUser = (ListView) view.findViewById(R.id.listView_user);
        adapter = new UserListViewAdapter(PeopleFragment.this);
        listViewUser.setAdapter(adapter);

        SharedPreferences spFollow = getActivity().getSharedPreferences(getArguments().getString("email"), Context.MODE_PRIVATE);
        Set<String> followingSet = spFollow.getStringSet("following", new HashSet<String>());

        File[] file = new File("/data/data/" + getActivity().getPackageName() + "/shared_prefs").listFiles();

        for (File user : file) {
            if(user.getName().contains("@")){
                String email = user.getName().split(".xml")[0];
                if(!email.equals(getArguments().getString("email"))){
                    SharedPreferences sp = getActivity().getSharedPreferences(email, Context.MODE_PRIVATE);
                    Uri uriProfileImage;
                    if (sp.getString("profile_image", "").equals("")) {
                        uriProfileImage = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.mipmap.ic_launcher);
                    } else {
                        uriProfileImage = Uri.parse(sp.getString("profile_image", ""));
                    }
                    adapter.addItem(uriProfileImage, sp.getString("nickname", ""), email, followingSet.contains(email));
                }
            }
        }

        return view;
    }

    public void addFollow(String email){
        SharedPreferences sp = getActivity().getSharedPreferences(getArguments().getString("email"), Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        Set<String> strFollowingList = sp.getStringSet("following", new HashSet<String>());
        strFollowingList.add(email);
        spEditor.putStringSet("following", strFollowingList);
        spEditor.apply();

        sp = getActivity().getSharedPreferences(email, Context.MODE_PRIVATE);
        spEditor = sp.edit();

        Set<String> strFollowerList = sp.getStringSet("follower", new HashSet<String>());
        strFollowerList.add(getArguments().getString("email"));
        spEditor.putStringSet("follower", strFollowerList);
        spEditor.apply();

    }

    public void removeFollow(String email){
        SharedPreferences sp = getActivity().getSharedPreferences(getArguments().getString("email"), Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        Set<String> strFollowingList = sp.getStringSet("following", new HashSet<String>());
        strFollowingList.remove(email);
        spEditor.putStringSet("following", strFollowingList);
        spEditor.apply();

        sp = getActivity().getSharedPreferences(email, Context.MODE_PRIVATE);
        spEditor = sp.edit();

        Set<String> strFollowerList = sp.getStringSet("follower", new HashSet<String>());
        strFollowerList.remove(getArguments().getString("email"));
        spEditor.putStringSet("follower", strFollowerList);
        spEditor.apply();
    }
}

package kr.co.teamnova.chad.capsule;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by Chad on 2017-01-18.
 */

public class UserListFragment extends Fragment {
    private static final String TAG = "UserListFragment";

    ListView listViewUser;
    UserListViewAdapter adapter;
    User loginUser;
    ArrayList<User> userList;

    public UserListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        loginUser = getArguments().getParcelable("login_user");
        listViewUser = (ListView) view.findViewById(R.id.listView_user);
        adapter = new UserListViewAdapter(UserListFragment.this, loginUser);
        listViewUser.setAdapter(adapter);
        final EditText editSearch = (EditText) view.findViewById(R.id.edit_search);
        final ImageButton ibtnCancel = (ImageButton) view.findViewById(R.id.ibtn_cancel);
        ibtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editSearch.setText("");
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (ibtnCancel.getVisibility() == View.GONE) {
                        ibtnCancel.setVisibility(View.VISIBLE);
                    }
                    String strFind = s.toString();
                    ArrayList<User> findUserList = new ArrayList<>();

                    for (User user : userList) {
                        if (user.getNickname().contains(strFind)) {
                            findUserList.add(user);
                        }
                    }

                    adapter.addUserList(findUserList);
                } else {
                    ibtnCancel.setVisibility(View.GONE);
                    adapter.addUserList(userList);
                }
            }
        });

        SharedPreferences spAccount = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);

        userList = new ArrayList<>();

        Set<String> allUser = spAccount.getAll().keySet();
        for (String email : allUser) {
            if (!loginUser.getEmail().equals(email)) {
                User user = new User(email, spAccount.getString(email, "").split(","));
                userList.add(user);
            }
        }

//        Collections.sort(userList, new UserListSort());

        adapter.addUserList(userList);

        return view;
    }

    class UserListSort implements Comparator<User> {
        public int compare(User user1, User user2) {
            return user1.getNickname().compareTo(user2.getNickname());
        }

    }
}

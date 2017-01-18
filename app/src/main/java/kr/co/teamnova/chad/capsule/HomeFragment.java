package kr.co.teamnova.chad.capsule;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Chad on 2017-01-18.
 */

public class HomeFragment extends Fragment {

    private TextView textView;

    private String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        email = getArguments().getString("email");
        textView = (TextView)view.findViewById(R.id.textView);
        textView.setText(email);

        return view;
    }
}

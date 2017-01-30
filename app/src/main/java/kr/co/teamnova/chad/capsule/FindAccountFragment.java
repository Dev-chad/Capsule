package kr.co.teamnova.chad.capsule;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Chad on 2017-01-30.
 */

public class FindAccountFragment extends Fragment {

    public FindAccountFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_email, container, false);
    }
}

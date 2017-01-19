package kr.co.teamnova.chad.capsule;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Chad on 2017-01-18.
 */

public class AddFragment extends Fragment {

    OnClickAddListener mCallback;

    public interface OnClickAddListener{
        public void AddClickEvent();
    }
    public AddFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        Button btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.AddClickEvent();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (OnClickAddListener) context;

        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "must implement OnArticleSelectedListener");
        }
    }
}

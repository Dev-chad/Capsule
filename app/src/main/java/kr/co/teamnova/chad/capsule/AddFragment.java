package kr.co.teamnova.chad.capsule;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Chad on 2017-01-18.
 */

public class AddFragment extends Fragment {

    private OnClickAddListener mCallback;
    private EditText editContentDetails;
    private EditText editTag;
    private ImageView imageContent;

    public interface OnClickAddListener{
        public void AddClickEvent();
    }
    public AddFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        editContentDetails = (EditText) view.findViewById(R.id.edit_content_details);
        editTag = (EditText) view.findViewById(R.id.edit_tag);
        imageContent = (ImageView) view.findViewById(R.id.image_content);
        imageContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
            }
        });
        Button btnUpload = (Button) view.findViewById(R.id.btn_add);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.AddClickEvent();
            }
        });

        Button btnTag = (Button) view.findViewById(R.id.btn_tag);
        btnTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

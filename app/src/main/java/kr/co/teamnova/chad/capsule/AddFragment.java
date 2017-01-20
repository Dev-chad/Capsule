package kr.co.teamnova.chad.capsule;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Chad on 2017-01-18.
 */

public class AddFragment extends Fragment {

    private final int PICK_FROM_ALBUM = 0;

    private String userEmail;
    private OnClickAddListener mCallback;
    private EditText editContentDetails;
    private EditText editTag;
    private ImageView imageContent;

    private Bitmap contentImage;

    public interface OnClickAddListener {
        public void AddClickEvent();
    }

    public AddFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        contentImage = null;
        userEmail = getArguments().getString("email");
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
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
            }
        });
        Button btnUpload = (Button) view.findViewById(R.id.btn_add);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int numOfContent;
                SharedPreferences userData = getActivity().getSharedPreferences(userEmail, MODE_PRIVATE);
                SharedPreferences.Editor userDataEditor = userData.edit();

                if (!userData.contains("num_of_content")) {
                    userDataEditor.putInt("num_of_content", 0);
                }

                numOfContent = userData.getInt("num_of_content", -1);
                numOfContent++;

                String contentDetail = editContentDetails.getText().toString();
                File userContentsDir = new File("/data/data/" + getActivity().getPackageName() + "/User/" + userEmail + "/Content");

                File savefile = new File(userContentsDir.getPath() + "/" + String.valueOf(numOfContent) + ".txt");

                try {
                    FileOutputStream fos = new FileOutputStream(savefile);
                    fos.write(contentDetail.getBytes());
                    fos.close();

                } catch (IOException e) {
                }

                if (contentImage != null) {
                    File copyFile = new File(userContentsDir.getPath() + "/" + String.valueOf(numOfContent) + ".jpg");
                    try {
                        copyFile.createNewFile();
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(copyFile));
                        contentImage.compress(Bitmap.CompressFormat.JPEG, 100, out);

                        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                userDataEditor.putInt("num_of_content", numOfContent);

                userDataEditor.apply();
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

        try {
            mCallback = (OnClickAddListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            contentImage = null;
            imageContent.setImageBitmap(null);
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {

                if (data.getData() != null) {
                    try {
                        contentImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageContent.setImageBitmap(contentImage);
                }
                break;
            }
        }

    }
}

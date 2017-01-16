package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Chad on 2017-01-16.
 */

public class JoinPageActivity extends AppCompatActivity {

    private final int PICK_FROM_ALBUM = 0;
    private final int CROP_FROM_IMAGE = 1;

    private Uri imageCaptureUri;

    private TextView textErrorMessage;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword ;
    private EditText editRePassword;
    private ImageView imageProfile;
    private EncryptData encryptData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page);

        textErrorMessage = (TextView) findViewById(R.id.text_error_message);
        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editRePassword = (EditText) findViewById(R.id.edit_retype_password);
        imageProfile = (ImageView) findViewById(R.id.image_profile);

        Button btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editFirstName.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.first_name_empty));
                } else if (editLastName.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.last_name_empty));
                } else if (editEmail.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.email_empty));
                } else if (editPassword.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.password_empty));
                } else if (editRePassword.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.password_empty));
                } else if (!editPassword.getText().toString().equals(editRePassword.getText().toString())) {
                    textErrorMessage.setText(getString(R.string.retype_password_error));
                    editPassword.setText("");
                    editRePassword.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), editFirstName.getText() + "의 캡슐이 생성되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "캡슐 생성을 취소합니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        Button btnImageSelect = (Button) findViewById(R.id.btn_image_select);
        btnImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);

                // TODO: This request code must change to constant variable.
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case PICK_FROM_ALBUM:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(data.getData(), "image/");

                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
            }
            case CROP_FROM_IMAGE:
            {
                final Bundle extras = data.getExtras();
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Capsule/Image/"+System.currentTimeMillis()+".jpg";
                System.out.println(filePath);
                Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();

                if(extras != null){
                    Bitmap photo = extras.getParcelable("data");

                    imageProfile.setImageBitmap(photo);

                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Capsule/Image";
                    File imageDirectory = new File(dirPath);
                    if(!imageDirectory.exists()){
                        Toast.makeText(this, "Not Exist", Toast.LENGTH_SHORT).show();
                        imageDirectory.mkdir();
                    }

                    File copyFile = new File(filePath);

                    try {
                        copyFile.createNewFile();
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(copyFile));
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, out);

                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
                        out.flush();
                        out.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}

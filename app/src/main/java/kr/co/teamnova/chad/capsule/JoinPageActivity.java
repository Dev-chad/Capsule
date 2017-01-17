package kr.co.teamnova.chad.capsule;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Chad on 2017-01-16.
 */

public class JoinPageActivity extends AppCompatActivity {

    private final int PICK_FROM_ALBUM = 0;
    private final int CROP_FROM_IMAGE = 1;

    boolean isDuplicatedNickname = false;

    private TextView textErrorMessage;
    private TextView textCheckNickname;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editRePassword;
    private EditText editNickname;
    private ImageView imageProfile;

    private Bitmap profileImage = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page);

        imageProfile = (ImageView) findViewById(R.id.image_profile);
        textErrorMessage = (TextView) findViewById(R.id.text_error_message);
        textCheckNickname = (TextView) findViewById(R.id.text_check_nickname);
        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editRePassword = (EditText) findViewById(R.id.edit_retype_password);

        editNickname = (EditText) findViewById(R.id.edit_nickname);
        editNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences sp = getSharedPreferences("Nickname", MODE_PRIVATE);
                if (sp.contains(editNickname.getText().toString())) {
                    textCheckNickname.setTextColor(0x99ff0000);
                    textCheckNickname.setText(R.string.str_not_available);
                    isDuplicatedNickname = true;
                } else if (editNickname.getText().toString().equals("")) {
                    textCheckNickname.setText("");
                    isDuplicatedNickname = false;
                } else {
                    textCheckNickname.setTextColor(0xff00ff00);
                    textCheckNickname.setText(R.string.str_available);
                    isDuplicatedNickname = false;
                }
            }
        });


        Button btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editFirstName.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.str_error_empty_first_name));
                } else if (editLastName.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.str_error_empty_last_name));
                } else if (editNickname.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.str_error_empty_nickname));
                } else if (isDuplicatedNickname) {
                    textErrorMessage.setText(getString(R.string.str_error_duplicated_nickname));
                } else if (editEmail.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.str_error_empty_email));
                } else if (editPassword.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.str_error_empty_password));
                } else if (editRePassword.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.str_error_empty_password));
                } else if (!editPassword.getText().toString().equals(editRePassword.getText().toString())) {
                    textErrorMessage.setText(getString(R.string.str_error_different_password));
                    editPassword.setText("");
                    editRePassword.setText("");
                } else {
                    File f = new File("/data/data/" + getPackageName() + "/shared_prefs/" + editEmail.getText().toString() + ".xml");
                    if (f.exists()) {
                        textErrorMessage.setText(getString(R.string.str_error_duplicated_email));
                        editEmail.setText("");
                    } else {
                        SharedPreferences profileData = getSharedPreferences(editEmail.getText().toString(), MODE_PRIVATE);
                        SharedPreferences nickNameData = getSharedPreferences("Nickname", MODE_PRIVATE);
                        SharedPreferences.Editor profileEditor = profileData.edit();
                        SharedPreferences.Editor nicknameEditor = nickNameData.edit();

                        profileEditor.putString("first_name", editFirstName.getText().toString());
                        profileEditor.putString("last_name", editLastName.getText().toString());
                        profileEditor.putString("nickname", editNickname.getText().toString());
                        nicknameEditor.putString(editNickname.getText().toString(), "");
                        profileEditor.putString("email", editEmail.getText().toString());
                        profileEditor.putString("password", EncryptData.getSHA256(editPassword.getText().toString()));

                        File userDir = new File("/data/data/" + getPackageName() + "/User/" + editEmail.getText().toString());
                        if (!userDir.exists()) {
                            userDir.mkdirs();
                        }

                        if (profileImage != null) {
                            File copyFile = new File(userDir.getPath(), "/profile.jpg");
                            try {
                                copyFile.createNewFile();
                                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(copyFile));
                                profileImage.compress(Bitmap.CompressFormat.JPEG, 100, out);

                                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            profileEditor.putString("profile_image", Uri.fromFile(copyFile).toString());
                        }
                        nicknameEditor.apply();
                        profileEditor.apply();
                        Toast.makeText(getApplicationContext(), editFirstName.getText() + getString(R.string.str_join_complete), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.str_join_cancel), Toast.LENGTH_LONG).show();
                finish();
            }
        });

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(data.getData(), "image/");

                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_IMAGE);
                break;
            }
            case CROP_FROM_IMAGE: {
                final Bundle extras = data.getExtras();

                if (extras != null) {
                    profileImage = extras.getParcelable("data");
                    imageProfile.setImageBitmap(profileImage);
                }
                break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                } else {
                    Toast.makeText(this, "권한 거부로 인해 갤러리에 접근을 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
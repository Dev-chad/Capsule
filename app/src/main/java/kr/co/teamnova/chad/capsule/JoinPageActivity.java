package kr.co.teamnova.chad.capsule;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by Chad on 2017-01-16.
 */

public class JoinPageActivity extends AppCompatActivity {
    private static final String TAG = "JoinPageActivity";

    private final int PICK_FROM_ALBUM = 0;
    private final int CROP_FROM_IMAGE = 1;
    private final int SEND_SMS = 2;

    private int authNum;

    private boolean isDuplicatedNickname = false;
    private boolean isCompletedPhoneAuth = false;

    private RelativeLayout layoutPhoneAuth;

    private TextView textErrorMessage;
    private TextView textCheckNickname;
    private TextView textAuthTime;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editRePassword;
    private EditText editNickname;
    private EditText editPhone;
    private EditText editAuth;
    private ImageView imageProfile;

    private Bitmap profileImage = null;

    private TimerAsyncTask authTimer;
    private Handler mHandler;
    private TimerThread thread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page);

        final SharedPreferences spAccount = getSharedPreferences("account", MODE_PRIVATE);

        authTimer = new TimerAsyncTask();
        thread = new TimerThread();
        layoutPhoneAuth = (RelativeLayout) findViewById(R.id.layout_phone_auth);

        editAuth = (EditText) findViewById(R.id.edit_auth_check);

        imageProfile = (ImageView) findViewById(R.id.image_profile);
        textErrorMessage = (TextView) findViewById(R.id.text_error_message);
        textCheckNickname = (TextView) findViewById(R.id.text_check_nickname);
        textAuthTime = (TextView) findViewById(R.id.text_time);
        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editRePassword = (EditText) findViewById(R.id.edit_retype_password);
        editPhone = (EditText) findViewById(R.id.edit_phone);

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
                SharedPreferences sp = getSharedPreferences("join", MODE_PRIVATE);
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

        final Button btnAuth = (Button) findViewById(R.id.btn_auth);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS);

                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, SEND_SMS);
                } else {
                    if (!editPhone.getText().toString().equals("")) {
                        editAuth.setText("");
                        sendAuthSMS();
                        if (layoutPhoneAuth.getVisibility() != View.VISIBLE) {
                            layoutPhoneAuth.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textErrorMessage.setText(R.string.str_error_empty_phone);
                    }
                }
            }
        });

        Button btnCheck = (Button) findViewById(R.id.btn_auth_check);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAuth.getText().toString().equals(String.valueOf(authNum))) {
                    if (!textAuthTime.getText().toString().equals("0:00")) {
//                        authTimer.cancel(true);
                        thread.setRunning(false);
                        thread.interrupt();
                        btnAuth.setClickable(false);
                        btnAuth.setBackgroundColor(0x9900ff00);
                        btnAuth.setText("완료");
                        editPhone.setFocusable(false);
                        editPhone.setClickable(false);
                        layoutPhoneAuth.setVisibility(View.GONE);
                        textErrorMessage.setText("");
                        isCompletedPhoneAuth = true;
                    } else {
                        textErrorMessage.setText(R.string.str_error_auth_overtime);
                    }
                } else {
                    editAuth.setText("");
                    textErrorMessage.setText(R.string.str_error_auth_incorrect);
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
                } else if (!isCompletedPhoneAuth) {
                    textErrorMessage.setText(R.string.str_error_phone_incomplete_auth);
                } else if (editPassword.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.str_error_empty_password));
                } else if (editRePassword.getText().toString().equals("")) {
                    textErrorMessage.setText(getString(R.string.str_error_empty_password));
                } else if (!editPassword.getText().toString().equals(editRePassword.getText().toString())) {
                    textErrorMessage.setText(getString(R.string.str_error_different_password));
                    editPassword.setText("");
                    editRePassword.setText("");
                } else {
                    if (spAccount.contains(editEmail.getText().toString())) {
                        textErrorMessage.setText(getString(R.string.str_error_duplicated_email));
                        editEmail.setText("");
                    } else {
                        SharedPreferences spJoin = getSharedPreferences("join", MODE_PRIVATE);
                        SharedPreferences.Editor spAccountEditor = spAccount.edit();
                        SharedPreferences.Editor spJoinEditor = spJoin.edit();

                        String strUriProfileImage;

                        File file = new File(getFilesDir() + "/profile_image");
                        try {
                            file.mkdirs();
                        } catch (Exception e) {
                            Log.e("error", e.toString());
                        }

                        File contentDir = new File(getFilesDir() + "/contents/" + editEmail.getText().toString());
                        if (!contentDir.exists()) {
                            contentDir.mkdirs();
                        }

                        if (profileImage != null) {
                            File copyFile = new File(file.getPath() + "/" + editEmail.getText().toString() + ".jpg");
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
                            strUriProfileImage = Uri.fromFile(copyFile).toString();
                        } else {
                            strUriProfileImage = Uri.parse("android.resource://" + getPackageName() + "/" + R.mipmap.image_user).toString();
                        }

                        String strUserData =
                                EncryptData.getSHA256(editPassword.getText().toString()) + ','
                                        + editLastName.getText().toString() + editFirstName.getText().toString() + ','
                                        + editPhone.getText().toString() + ','
                                        + editNickname.getText().toString() + ','
                                        + strUriProfileImage + ",0, , ";

                        spAccountEditor.putString(editEmail.getText().toString(), strUserData);
                        spJoinEditor.putString(editNickname.getText().toString(), "");

                        Log.d(TAG, strUserData);

                        spJoinEditor.apply();
                        spAccountEditor.apply();
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

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                textAuthTime.setText(msg.getData().getString("time"));
            }
        };
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
                break;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendAuthSMS();
                    layoutPhoneAuth.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (profileImage != null) {
            imageProfile.setImageBitmap(profileImage);
        }
    }

    private void sendAuthSMS() {
        try {
            Random rand = new Random();
            authNum = rand.nextInt();
            authNum = (authNum >>> 1) % (500000 - 100000) + 100000;

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(editPhone.getText().toString(), null, "[CAPSULE]\n본인인증번호는 " + authNum + " 입니다.\n정확히 입력해주세요.", null, null);


            if(thread.running){
                thread.setRunning(false);
                thread.interrupt();
            }
            thread = new TimerThread();
            thread.setRunning(true);
            thread.start();
            /*if (authTimer.getStatus() == AsyncTask.Status.RUNNING) {
                authTimer.cancel(true);
            }
            authTimer = new TimerAsyncTask();
            authTimer.execute();*/
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }

    }

    public class TimerThread extends Thread {

        boolean running = false;
        int m = 0;
        int s = 10;

        void setRunning(boolean b) {
            running = b;
        }

        @Override
        public void run() {
            Message msg;
            Bundle data = new Bundle();
            while (running && m > -1) {
                try {
                    data.putString("time", String.format("%d:%02d", m, s));
                    Log.d(TAG, String.format("%d:%02d", m, s));
                    msg = mHandler.obtainMessage();
                    msg.setData(data);
                    mHandler.sendMessage(msg);
                    if (--s < 0) {
                        s = 59;
                        --m;
                    }
                    sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    public class TimerAsyncTask extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... value) {
            int m = 0;
            int s = 10;

            while (m > -1) {
                if (isCancelled()) {
                    break;
                }
                publishProgress(String.format("%d:%02d", m, s));
                if (--s < 0) {
                    s = 59;
                    --m;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textAuthTime.setText(values[0]);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }



    }
}
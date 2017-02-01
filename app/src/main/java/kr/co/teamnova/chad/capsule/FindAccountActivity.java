package kr.co.teamnova.chad.capsule;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Random;

/**
 * Created by Chad on 2017-01-30.
 */

public class FindAccountActivity extends AppCompatActivity {
    private final int SEND_SMS = 2;

    private int authNum;
    private boolean isCompletedPhoneAuth = false;

    private TimerAsyncTask authTimer;

    private RadioButton rbtnEmail;
    private RadioButton rbtnPassword;

    private RelativeLayout layoutMain;
    private RelativeLayout layoutPhoneAuth;

    private EditText editEmail;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPhone;
    private EditText editAuthNum;
    private EditText editPassword;
    private EditText editRePassword;

    private TextView textAuthTime;
    private TextView textError;

    private Button btnAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        authTimer = new TimerAsyncTask();

        textAuthTime = (TextView) findViewById(R.id.text_time);
        textError = (TextView) findViewById(R.id.text_error_message);

        editEmail = (EditText) findViewById(R.id.edit_email);
        editAuthNum = (EditText) findViewById(R.id.edit_auth_check);
        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editPhone = (EditText) findViewById(R.id.edit_phone);

        layoutPhoneAuth = (RelativeLayout) findViewById(R.id.layout_phone_auth);
        layoutMain = (RelativeLayout) findViewById(R.id.layout_main);

        rbtnEmail = (RadioButton) findViewById(R.id.rbtn_find_email);
        rbtnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewDefault();
                if (layoutMain.getVisibility() == View.GONE) {
                    layoutMain.setVisibility(View.VISIBLE);
                }

                if (editEmail.getVisibility() == View.VISIBLE) {
                    editEmail.setVisibility(View.GONE);
                }
            }
        });

        rbtnPassword = (RadioButton) findViewById(R.id.rbtn_find_password);
        rbtnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewDefault();
                if (layoutMain.getVisibility() == View.GONE) {
                    layoutMain.setVisibility(View.VISIBLE);
                }

                if (editEmail.getVisibility() == View.GONE) {
                    editEmail.setVisibility(View.VISIBLE);
                }
            }
        });

        btnAuth = (Button) findViewById(R.id.btn_auth);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS);

                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, SEND_SMS);
                } else {
                    if (!editPhone.getText().toString().equals("")) {
                        editAuthNum.setText("");
                        sendAuthSMS();
                        if (layoutPhoneAuth.getVisibility() != View.VISIBLE) {
                            layoutPhoneAuth.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textError.setText(R.string.str_error_empty_phone);
                    }
                }
            }
        });

        final Button btnAuthCheck = (Button) findViewById(R.id.btn_auth_check);
        btnAuthCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAuthNum.getText().toString().equals(String.valueOf(authNum))) {
                    if (!textAuthTime.getText().toString().equals("0:00")) {
                        authTimer.cancel(true);
                        btnAuth.setClickable(false);
                        btnAuth.setText("완료");
                        editPhone.setFocusable(false);
                        editPhone.setClickable(false);
                        layoutPhoneAuth.setVisibility(View.GONE);
                        textError.setText("");
                        isCompletedPhoneAuth = true;
                    } else {
                        textError.setText(R.string.str_error_auth_overtime);
                    }
                } else {
                    editAuthNum.setText("");
                    textError.setText(R.string.str_error_auth_incorrect);
                }
            }
        });

        final Button btnFind = (Button) findViewById(R.id.btn_find);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editFirstName.length() == 0) {
                    textError.setText(R.string.str_error_empty_first_name);
                } else if (editLastName.length() == 0) {
                    textError.setText(R.string.str_error_empty_last_name);
                } else if (rbtnPassword.isChecked() && editEmail.length() == 0) {
                    textError.setText(R.string.str_error_empty_email);
                } else if (!isCompletedPhoneAuth) {
                    textError.setText(R.string.str_error_phone_incomplete_auth);
                } else {
                    if (rbtnEmail.isChecked()) {
                        boolean isFind = false;
                        String email = "";
                        File[] f = new File("/data/data/" + getPackageName() + "/shared_prefs").listFiles();
                        if (f != null) {
                            for (File ff : f) {
                                email = ff.getName().split(".xml")[0];
                                SharedPreferences sp = getSharedPreferences(email, MODE_PRIVATE);
                                if (sp.contains("phone")) {
                                    if (sp.getString("phone", "").equals(editPhone.getText().toString()) && sp.getString("first_name", "").equals(editFirstName.getText().toString()) && sp.getString("last_name", "").equals(editLastName.getText().toString())) {
                                        isFind = true;
                                        break;
                                    }
                                }
                            }

                            if (isFind) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(FindAccountActivity.this);
                                alert
                                        .setTitle("이메일 찾기")
                                        .setMessage(email)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                setViewDefault();
                                                dialog.dismiss();     //닫기
                                            }
                                        });
                                AlertDialog messageBox = alert.create();
                                messageBox.show();
                            } else {
                                Toast.makeText(FindAccountActivity.this, R.string.str_error_user_found, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FindAccountActivity.this, R.string.str_error_user_found, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        File f = new File("/data/data/" + getPackageName() + "/shared_prefs/" + editEmail.getText().toString() + ".xml");
                        if (f.exists()) {
                            final SharedPreferences sp = getSharedPreferences(editEmail.getText().toString(), MODE_PRIVATE);
                            if (sp.getString("first_name", "").equals(editFirstName.getText().toString()) && sp.getString("last_name", "").equals(editLastName.getText().toString()) && sp.getString("phone", "").equals(editPhone.getText().toString())) {
                                LayoutInflater inflater = getLayoutInflater();

                                final View dialogView = inflater.inflate(R.layout.dialog_change_password, null);

                                editPassword = (EditText) dialogView.findViewById(R.id.edit_password);
                                editRePassword = (EditText) dialogView.findViewById(R.id.edit_retype_password);

                                AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(FindAccountActivity.this);
                                dlgBuilder.setTitle("비밀번호 변경");
                                dlgBuilder.setView(dialogView);
                                dlgBuilder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                dlgBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        setViewDefault();
                                    }
                                });

                                final AlertDialog dlgChangePassword = dlgBuilder.create();
                                dlgChangePassword.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(final DialogInterface dialog) {
                                        Button btnChange = dlgChangePassword.getButton(DialogInterface.BUTTON_POSITIVE);
                                        btnChange.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String strPassword = editPassword.getText().toString();

                                                if(strPassword.equals(editRePassword.getText().toString())){
                                                    SharedPreferences.Editor spEditor = sp.edit();
                                                    spEditor.putString("password", EncryptData.getSHA256(strPassword));
                                                    spEditor.apply();
                                                    dialog.dismiss();
                                                    Toast.makeText(FindAccountActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(FindAccountActivity.this, R.string.str_error_incorrect_password, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                });
                                dlgChangePassword.show();

                            } else {
                                Toast.makeText(FindAccountActivity.this, R.string.str_error_user_found, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(FindAccountActivity.this, R.string.str_error_user_found, Toast.LENGTH_SHORT).show();
                        }

                   /* File f = new File("/data/data/" + getPackageName() + "/shared_prefs/" + editEmail.getText().toString() + ".xml");
                    if (f.exists()) {
                        SharedPreferences profileData = getSharedPreferences(editEmail.getText().toString(), MODE_PRIVATE);
                        if (EncryptData.getSHA256(editPassword.getText().toString()).equals(profileData.getString("password", ""))) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("email", editEmail.getText().toString());
                            startActivity(intent);
                        } else {
                            textError.setText(getString(R.string.str_error_incorrect_password));
                            editPassword.setText("");
                        }
                    } else {
                        textError.setText(getString(R.string.str_error_nonexistent_email));
                    }*/
                    }
                }
            }
        });
    }

    private void sendAuthSMS() {
        try {
            Random rand = new Random();
            authNum = rand.nextInt();
            authNum = (authNum >>> 1) % (500000 - 100000) + 100000;

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(editPhone.getText().toString(), null, "[CAPSULE]\n본인인증번호는 " + authNum + " 입니다.\n정확히 입력해주세요.", null, null);

            if (authTimer.getStatus() == AsyncTask.Status.RUNNING) {
                authTimer.cancel(true);
            }
            authTimer = new TimerAsyncTask();
            authTimer.execute();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void setViewDefault() {
        if (authTimer.getStatus() == AsyncTask.Status.RUNNING) {
            authTimer.cancel(true);
        }
        editFirstName.setText("");
        editLastName.setText("");

        editPhone.setText("");
        editPhone.setClickable(true);
        editPhone.setFocusableInTouchMode(true);
        editPhone.setFocusable(true);


        editEmail.setText("");
        editAuthNum.setText("");

        btnAuth.setText(R.string.str_auth);
        btnAuth.setClickable(true);

        isCompletedPhoneAuth = false;

        if (layoutPhoneAuth.getVisibility() == View.VISIBLE) {
            layoutPhoneAuth.setVisibility(View.GONE);
        }
        textError.setText("");
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

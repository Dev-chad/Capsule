package kr.co.teamnova.chad.capsule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Chad on 2017-01-30.
 */

public class FindAccountActivity extends AppCompatActivity {

    private int authNum;

    private TimerAsyncTask authTimer = null;

    private RadioButton rbtnEmail;
    private RadioButton rbtnPassword;

    private RelativeLayout layoutMain;
    private RelativeLayout layoutPhoneAuth;

    private EditText editEmail;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editPhone;
    private EditText editAuthNum;

    private TextView textAuthTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        textAuthTime = (TextView) findViewById(R.id.text_time);

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
                if (layoutMain.getVisibility() == View.GONE) {
                    layoutMain.setVisibility(View.VISIBLE);
                }

                if (editEmail.getVisibility() == View.GONE) {
                    editEmail.setVisibility(View.VISIBLE);
                }
            }
        });

        Button btnAuth = (Button) findViewById(R.id.btn_auth);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btnAuthCheck = (Button) findViewById(R.id.btn_auth_check);
        btnAuthCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btnFind = (Button) findViewById(R.id.btn_find);
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbtnEmail.isChecked()) {

                } else {

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
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
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

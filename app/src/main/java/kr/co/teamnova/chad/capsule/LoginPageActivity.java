package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class LoginPageActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        String email = getSharedPreferences("login_info", MODE_PRIVATE).getString("email", "");

        editEmail = (EditText) findViewById(R.id.edit_email);
        if(email.length() > 0){
            editEmail.setText(email);
        }

        editPassword = (EditText) findViewById(R.id.edit_password);
        textError = (TextView) findViewById(R.id.text_error_message);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editEmail.getText().toString().equals("")) {
                    textError.setText(getString(R.string.str_error_empty_email));
                } else if (editPassword.getText().toString().equals("")) {
                    textError.setText(getString(R.string.str_error_empty_password));
                } else {
                    File f = new File("/data/data/" + getPackageName() + "/shared_prefs/" + editEmail.getText().toString() + ".xml");
                    if (f.exists()) {
                        SharedPreferences profileData = getSharedPreferences(editEmail.getText().toString(), MODE_PRIVATE);
                        if (EncryptData.getSHA256(editPassword.getText().toString()).equals(profileData.getString("password", ""))) {
                            SharedPreferences autoLoginData = getSharedPreferences("login_info", MODE_PRIVATE);
                            SharedPreferences.Editor autoLoginEditor = autoLoginData.edit();
                            autoLoginEditor.putString("email", editEmail.getText().toString());
                            autoLoginEditor.putString("enable", "true");
                            autoLoginEditor.apply();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("email", editEmail.getText().toString());
                            startActivity(intent);
                            finish();
                        } else {
                            textError.setText(getString(R.string.str_error_incorrect_password));
                            editPassword.setText("");
                        }
                    } else {
                        textError.setText(getString(R.string.str_error_nonexistent_email));
                    }
                }
            }
        });

        Button btnJoin = (Button) findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JoinPageActivity.class);
                startActivity(intent);
            }
        });

        TextView strForgotAccount = (TextView) findViewById(R.id.text_forgotAccount);
        strForgotAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        editPassword.setText("");
    }
}

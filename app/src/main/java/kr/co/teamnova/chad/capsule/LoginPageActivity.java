package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

// TODO: Make solution about that how to send instance(class) using intent.
public class LoginPageActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private TextView textError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        textError = (TextView) findViewById(R.id.text_error_message);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = new File("/data/data/" + getPackageName() + "/shared_prefs/" + editEmail.getText().toString() + ".xml");
                if(f.exists()){
                    SharedPreferences profileData = getSharedPreferences(editEmail.getText().toString(), MODE_PRIVATE);

                    if(EncryptData.getSHA256(editPassword.getText().toString()).equals(profileData.getString("password", ""))){
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("email", editEmail.getText().toString());
                        startActivity(intent);
                    } else {
                        textError.setText(getString(R.string.str_error_incorrect_password));
                        editPassword.setText("");
                    }
                } else {
                    textError.setText(getString(R.string.str_error_nonexistent_email));
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
                Toast.makeText(getApplicationContext(), "Hope find it.", Toast.LENGTH_LONG).show();
            }
        });
    }
}

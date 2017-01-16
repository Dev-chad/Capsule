package kr.co.teamnova.chad.capsule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Make solution about that how to send instance(class) using intent.
public class LoginPageActivity extends AppCompatActivity {

    private EncryptData encryptData;
    private EditText editEmail;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        encryptData = new EncryptData();

        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Email: " + editEmail.getText() + "\nPassword(Plain): " + editPassword.getText() + "\nPassword(Cipher):" + encryptData.getSHA256(editPassword.getText().toString()), Toast.LENGTH_LONG).show();
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

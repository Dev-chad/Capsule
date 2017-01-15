package kr.co.teamnova.chad.capsule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Chad on 2017-01-16.
 */

public class JoinPageActivity extends AppCompatActivity {
    private TextView textErrorMessage;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword ;
    private EditText editRePassword;
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
    }
}

package com.joey.myim.pages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.joey.myim.service.Connect;
import com.joey.myim.R;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity {
    EditText mEtRegisterUsername, mEtRegisterPassword;
    Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEtRegisterUsername = findViewById(R.id.et_register_username);
        mEtRegisterPassword = findViewById(R.id.et_register_password);
        mBtnRegister = findViewById(R.id.btn_register);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEtRegisterUsername.getText().toString().trim();
                String password = mEtRegisterPassword.getText().toString().trim();

                if (Connect.register(username,password)) {
                    Connect.login(username,password);
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("username",mEtRegisterUsername.getText());
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
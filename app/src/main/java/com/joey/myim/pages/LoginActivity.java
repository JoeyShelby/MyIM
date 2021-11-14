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
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {
    EditText mEtLoginUsername, mEtLoginPassword;
    Button mBtnLogin,mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtLoginUsername = findViewById(R.id.et_login_username);
        mEtLoginPassword = findViewById(R.id.et_login_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEtLoginUsername.getText().toString().trim();
                String password = mEtLoginPassword.getText().toString().trim();

                if(Connect.login(username,password)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username",mEtLoginUsername.getText().toString());
                    startActivity(intent);
                    mEtLoginPassword.setText("");
                }else{
                    Toast.makeText(getApplicationContext(),"登陆失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
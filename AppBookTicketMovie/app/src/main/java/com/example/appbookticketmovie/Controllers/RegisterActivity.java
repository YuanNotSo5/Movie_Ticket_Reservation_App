package com.example.appbookticketmovie.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.appbookticketmovie.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etFullname, etPassword, etConfirmPassword;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.register_et_username);
        etEmail = findViewById(R.id.register_et_email);
        etFullname = findViewById(R.id.register_et_fullname);
        etPassword = findViewById(R.id.register_et_password);
        etConfirmPassword = findViewById(R.id.register_et_confirm_password);

        btnRegister = findViewById(R.id.button);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String fullname = etFullname.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPass = etConfirmPassword.getText().toString();

                //if(username)
            }
        });

    }
}
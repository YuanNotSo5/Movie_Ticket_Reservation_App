package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.appbookticketmovie.R;
import com.google.android.material.textfield.TextInputEditText;

public class ResetPWActivity extends AppCompatActivity {

    TextView tvLogin, tvRegister;
    Button reset;
    TextInputEditText reset_et_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwactivity);

        tvLogin = findViewById(R.id.tvLogin);
        tvRegister = findViewById(R.id.tvRegister);
        reset_et_email = findViewById(R.id.reset_et_email);
        reset = findViewById(R.id.)
    }
}
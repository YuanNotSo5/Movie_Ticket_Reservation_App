package com.example.appbookticketmovie.HomeActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.UserService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPWActivity extends AppCompatActivity {

    TextView tvLogin, tvRegister;
    Button reset;
    TextInputEditText reset_et_email;

    private UserService userService;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwactivity);

        tvLogin = findViewById(R.id.tvLogin);
        tvRegister = findViewById(R.id.tvRegister);
        reset_et_email = findViewById(R.id.reset_et_email);
        reset = findViewById(R.id.btnReset);

        userService = new UserService();
        firebaseAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = true;
                String email = reset_et_email.getText().toString().trim();
                if(email.equals("")){
                    reset_et_email.setError("Please Enter Email");
                    reset_et_email.setEnabled(true);
                    status = false;
                }
                if(status){
                    userService.checkEmail(email, new UserService.checkEmailExisted() {
                        @Override
                        public void onSuccess(boolean status) {
                            if(status){
                                sendEmail(email);
                            }
                            else{
                                Toast.makeText(ResetPWActivity.this, "No Account Exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Log.d("Reset PWD Error", e.getMessage());
                        }
                    });
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ResetPWActivity.this, RegisterActivity.class);
                startActivity(login);
                finish();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(ResetPWActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });
    }

    public void sendEmail(String email){
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetPWActivity.this, "Link For Password Reset Has Been Sent To Your Email", Toast.LENGTH_SHORT).show();
                        Intent login = new Intent(ResetPWActivity.this, LoginActivity.class);
                        startActivity(login);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPWActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
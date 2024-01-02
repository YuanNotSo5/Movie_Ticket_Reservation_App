package com.example.appbookticketmovie.HomeActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                String email = tvRegister.getText().toString();
                if(email.equals("")){
                    reset_et_email.setError("Please Enter Email");
                    reset_et_email.setEnabled(true);
                    status = false;
                }
                if(status){
                    sendEmail(email);
                }
            }
        });
    }

    public void sendEmail(String email){
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
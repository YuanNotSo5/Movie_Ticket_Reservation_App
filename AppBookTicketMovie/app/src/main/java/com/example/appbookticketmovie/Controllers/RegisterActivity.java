package com.example.appbookticketmovie.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etFullname, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView warning, login;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userDB = db.collection("Users");

        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etFullname = findViewById(R.id.et_fullname);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        warning = findViewById(R.id.textView2);
        login = findViewById(R.id.textView3);

        btnRegister = findViewById(R.id.button);

        warning.setVisibility(View.GONE);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = true;

                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String fullname = etFullname.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPass = etConfirmPassword.getText().toString();


                if(username.equals("")){
                    etUsername.setError("Please Enter Username");
                    etUsername.setEnabled(true);
                    status = false;
                }
                if(email.equals("")){
                    etEmail.setError("Please Enter Email");
                    etEmail.setEnabled(true);
                    status = false;
                }
                if(fullname.equals("")){
                    etFullname.setError("Please Enter Your Full Name");
                    etFullname.setEnabled(true);
                    status = false;
                }
                if(password.equals("")){
                    etPassword.setError("Please Enter Password");
                    etPassword.setEnabled(true);
                    status = false;
                }

                if(confirmPass.equals("")){
                    etConfirmPassword.setError("Please Enter Confirm Password");
                    etConfirmPassword.setEnabled(true);
                    status = false;
                }

                if(password.toCharArray().length < 6){
                    etPassword.setError("Your Password Must Be More Than 6 Character");
                    etPassword.setEnabled(true);
                    status = false;
                }

                if(!password.equals(confirmPass)){
                    warning.setVisibility(View.VISIBLE);
                    status = false;
                }

                if(status){
                    String count = String.valueOf(countUser());
                    User user = new User(Long.valueOf(count)+1,username, email, fullname);

                    DocumentReference result = userDB.document(email);

                    result.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    warning.setVisibility(View.GONE);

                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {
                                                    Toast.makeText(RegisterActivity.this, "Create Account Successfully", Toast.LENGTH_SHORT).show();
                                                    warning.setVisibility(View.GONE);

                                                    Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(login);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    System.out.println(e);
                                                    Toast.makeText(RegisterActivity.this, "Create New Account Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("Error: "+e);
                                    Toast.makeText(RegisterActivity.this, "Create New User Failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

    }

    public int countUser(){
        Task<QuerySnapshot> data = userDB.get();
        if(data.isSuccessful()){
            List<DocumentSnapshot> d = data.getResult().getDocuments();
            return ((List<?>) d).size();
        }
        return 0;
    }
}
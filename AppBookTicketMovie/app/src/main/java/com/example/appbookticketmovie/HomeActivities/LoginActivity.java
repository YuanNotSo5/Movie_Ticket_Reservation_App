package com.example.appbookticketmovie.HomeActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.appbookticketmovie.MainActivity;
import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.UserService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin, googleLogin;
    TextView warning, register, resetPwd;
    private FirebaseAuth firebaseAuth;
    private static final int REQ_SIGN_IN = 999;
    private GoogleSignInClient googleSignInClient;

    private int count = 0;

    private FirebaseFirestore db;

    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userService = new UserService();

        etEmail = findViewById(R.id.login_et_email);
        etPassword = findViewById(R.id.login_et_password);

        warning = findViewById(R.id.textView6);
        register = findViewById(R.id.textView4);

        btnLogin = findViewById(R.id.button);
        googleLogin = findViewById(R.id.button4);

        resetPwd = findViewById(R.id.textView30);

        countUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reset = new Intent(LoginActivity.this, ResetPWActivity.class);
                startActivity(reset);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = true;
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(email.equals("")){
                    etEmail.setError("Please Enter Email");
                    etEmail.setEnabled(true);
                    status = false;
                }

                if(password.equals("")){
                    etPassword.setError("Please Enter Password");
                    etPassword.setEnabled(true);
                    status = false;
                }

                if(status){
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    warning.setVisibility(View.GONE);
                                    Intent home = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(home);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    warning.setVisibility(View.VISIBLE);
                                    System.out.println(e);
                                }
                            });
                };
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, REQ_SIGN_IN);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_SIGN_IN:
                try {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    GoogleSignInAccount account = task.getResult(ApiException.class);

                    userService.checkEmail(account.getEmail(), new UserService.checkEmailExisted() {
                        @Override
                        public void onSuccess(boolean status) {
                            if(!status)
                                createAccount(account);
                            else{
                                googleSignIn(account);
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });


                } catch (ApiException e) {
                    System.out.println(e);
                }
                break;
        }
    }

    public void googleSignIn(GoogleSignInAccount account){
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(firebaseCredential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        System.out.println(user.getDisplayName());

                        Intent home = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(home);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e);
                    }
                });
    }

    public void createAccount(GoogleSignInAccount account){
        String c = String.valueOf(count);
        User user = new User(Long.valueOf(c),account.getDisplayName(), account.getEmail(), account.getDisplayName());

        db.collection("Users").document(account.getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        googleSignIn(account);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Error: "+e);
                        Toast.makeText(LoginActivity.this, "Create New User Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void countUser(){
        Task<QuerySnapshot> data = db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    count = task.getResult().size();
                    count++;
                }
            }
        });
    }
}
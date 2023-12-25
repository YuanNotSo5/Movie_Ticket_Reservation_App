package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.UserService;
import com.google.android.material.textfield.TextInputLayout;

public class UpdateAccountActivity extends AppCompatActivity {
    TextView tvUsername;
    EditText etUsername, etEmail, etFullname, etPhone, etNewPassword, etPasswordConfirm;
    TextInputLayout  etPasswordConfirmLayout, etNewPasswordLayout;
    Button updateBtn;
    ImageView backImg2, settingsBtn2, resetPwd;
    WebView avatar;
    private UserService userService;
    private boolean status = false;
    private boolean changePwd = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        userService = new UserService();

        tvUsername = findViewById(R.id.tvUsername);
        etUsername = findViewById(R.id.et_username_info);
        etEmail = findViewById(R.id.et_email_info);
        etFullname = findViewById(R.id.et_fullname_info);
        etPhone = findViewById(R.id.et_phone_info);
        etNewPassword = findViewById(R.id.et_password_info);
        etPasswordConfirm = findViewById(R.id.et_confirm_password_info);

        etNewPasswordLayout = findViewById(R.id.register_et_password_info);
        etPasswordConfirmLayout = findViewById(R.id.register_et_confirm_password_info);

        avatar = findViewById(R.id.avatar_info);
        updateBtn = findViewById(R.id.updateBtn);

        settingsBtn2 = findViewById(R.id.settingsBtn2);
        backImg2 = findViewById(R.id.backImg2);
        resetPwd = findViewById(R.id.resetPwd);

        getUserInfo();

        etUsername.setEnabled(false);
        etEmail.setEnabled(false);
        etFullname.setEnabled(false);
        etPhone.setEnabled(false);

        backImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        settingsBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwd = false;
                if(status){
                    status = false;
                    etUsername.setEnabled(false);
                    etEmail.setEnabled(false);
                    etFullname.setEnabled(false);
                    etPhone.setEnabled(false);
                    updateBtn.setVisibility(View.GONE);
                }
                else{
                    status = true;
                    etUsername.setEnabled(true);
                    etFullname.setEnabled(true);
                    etPhone.setEnabled(true);
                    updateBtn.setVisibility(View.VISIBLE);
                }

                etUsername.setVisibility(View.VISIBLE);
                etEmail.setVisibility(View.VISIBLE);
                etFullname.setVisibility(View.VISIBLE);
                etPhone.setVisibility(View.VISIBLE);

                etNewPasswordLayout.setVisibility(View.GONE);
                etPasswordConfirmLayout.setVisibility(View.GONE);
            }
        });

        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwd = true;
                etNewPasswordLayout.setVisibility(View.VISIBLE);
                etPasswordConfirmLayout.setVisibility(View.VISIBLE);

                etUsername.setVisibility(View.GONE);
                etEmail.setVisibility(View.GONE);
                etFullname.setVisibility(View.GONE);
                etPhone.setVisibility(View.GONE);

                updateBtn.setVisibility(View.VISIBLE);
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String fullname = etFullname.getText().toString();
                String phoneNumber = etPhone.getText().toString();

                String newPwd = etNewPassword.getText().toString();
                String pwdConfirm = etPasswordConfirm.getText().toString();

                if (changePwd) {
                    boolean statusChange = true;
                    if (newPwd.equals("")) {
                        etNewPassword.setError("Please Enter New Password");
                        etNewPassword.setEnabled(true);
                        statusChange = false;
                    }
                    if (pwdConfirm.equals("")) {
                        etPasswordConfirm.setError("Please Enter Password To Confirm");
                        etPasswordConfirm.setEnabled(true);
                        statusChange = false;
                    }

                    if (!pwdConfirm.equals(newPwd)) {
                        etPasswordConfirm.setError("Passwords are not matched");
                        etPasswordConfirm.setEnabled(true);
                        statusChange = false;
                    }

                    if (statusChange) {
                        userService.updatePassword(newPwd, new UserService.passwordUpdate() {
                            @Override
                            public void updatePassword(boolean status) {
                                Toast.makeText(UpdateAccountActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(UpdateAccountActivity.this, "There's something wrong when updating your password", Toast.LENGTH_SHORT).show();
                                Log.d("Update User Error: ", e.getMessage());
                            }
                        });
                    }
                } else {
                    User updatedUser = new User(username, email, fullname, phoneNumber);
                    userService.updateUser(updatedUser, new UserService.editUserInfo() {
                        @Override
                        public void updateUser(boolean status) {
                            if (status) {
                                Toast.makeText(UpdateAccountActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(UpdateAccountActivity.this, "There's something wrong when updating user info", Toast.LENGTH_SHORT).show();
                            Log.d("Update User Error: ", e.getMessage());
                        }
                    });
                }
            }
        });


    }
    public void getUserInfo(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                userService.getUserByEmail(new UserService.getUser() {
                    @Override
                    public void getUser(User user) {
                        System.out.println(user);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvUsername.setText(user.getUsername());
                                etUsername.setText(user.getUsername());
                                etEmail.setText(user.getEmail());
                                etFullname.setText(user.getFullname());
                                etPhone.setText(user.getPhoneNumber());

                                String html = "<html><body><img src='" + user.getAvatar()  + "'> </body></html>";
                                avatar.loadDataWithBaseURL(null, "<style>img{height: auto;max-width: 100%;}</style>" + html, "text/html", "UTF-8", null);
                            }
                        });
                    }
                    @Override
                    public void onError(String errorMessage) {
                        Log.d("Get User Error:", errorMessage);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("Get User Failed:", e.getMessage());
                    }
                });
            }
        }).start();
    }
}
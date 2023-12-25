package com.example.appbookticketmovie.ui.user;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.appbookticketmovie.HomeActivities.LoginActivity;
import com.example.appbookticketmovie.HomeActivities.RegisterActivity;
import com.example.appbookticketmovie.HomeActivities.UpdateAccountActivity;
import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.Services.UserService;
import com.example.appbookticketmovie.databinding.FragmentUserBinding;
import com.google.firebase.auth.FirebaseAuth;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private UserViewModel userViewModel;
    private VideoView backgroundView;
    private FirebaseAuth firebaseAuth;
    private UserService userService;
    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageView settingBtn = binding.settingsBtn;
        ImageView logoutBtn = binding.logoutBtn;

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setting = new Intent(requireContext(), UpdateAccountActivity.class);
                startActivityForResult(setting, 000);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Logout", "Clicked");

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
                dialogBuilder.setTitle("Do You Want To Log Out?");
                dialogBuilder.setCancelable(true);
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();

                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                });
                dialogBuilder.create().show();
            }
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            displaySelectionDialog();
        }
        else{
            getUserInfo();
        }
    }

    public void getUserInfo(){
        userService = new UserService();

        new Thread(new Runnable() {
            @Override
            public void run() {
                userService.getUserByEmail(new UserService.getUser() {
                    @Override
                    public void getUser(User user) {

                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView username = binding.textView8;
                                username.setText(user.getUsername());

                                TextView point = binding.tvPoint;
                                point.setText(String.valueOf(user.getPoint()));

                                WebView avatar = binding.avatar;
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
    public void displaySelectionDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setTitle("You are not logged in");
        dialogBuilder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent login = new Intent(requireContext(), LoginActivity.class);
                startActivity(login);
            }
        });
        dialogBuilder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent register = new Intent(requireContext(), RegisterActivity.class);
                startActivity(register);
            }
        });
        dialogBuilder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 000){
            getActivity().finish();
            startActivity(getActivity().getIntent());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
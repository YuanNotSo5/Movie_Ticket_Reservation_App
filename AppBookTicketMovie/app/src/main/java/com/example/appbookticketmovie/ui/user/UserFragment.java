package com.example.appbookticketmovie.ui.user;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.appbookticketmovie.Adapter.FavoriteFilmAdapter;
import com.example.appbookticketmovie.Adapter.HistoryBillAdapter;
import com.example.appbookticketmovie.HomeActivities.LoginActivity;
import com.example.appbookticketmovie.HomeActivities.RegisterActivity;
import com.example.appbookticketmovie.HomeActivities.UpdateAccountActivity;
import com.example.appbookticketmovie.Models.ListBill;
import com.example.appbookticketmovie.Models.ListFilmFavorite;
import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.FilmService;
import com.example.appbookticketmovie.Services.UserService;
import com.example.appbookticketmovie.databinding.FragmentUserBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    private long idUser = -1;
    private FragmentUserBinding binding;
    private UserViewModel userViewModel;
    private VideoView backgroundView;
    private FirebaseAuth firebaseAuth;

    private GoogleSignInClient googleSignInClient;
    private UserService userService;
    private FilmService filmService = new FilmService();
    private LinearLayout historyBtn, favBtn, pointBtn;
    private RecyclerView containerContent;
    private RecyclerView.Adapter adapterContainer;
    private boolean isHistoryBtnClicked = false;
    private boolean isFavBtnClicked = false;
    private boolean isPointBtnClicked = false;

    private int favorites = 0;
    private int bills = 0;

    TextView tvFavorite, tvTickets;
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

        tvFavorite = binding.tvFavorite;
        tvTickets = binding.tvTickets;


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
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.client_id))
                                .requestEmail()
                                .build();
                        googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

                        googleSignInClient.signOut().addOnCompleteListener(getActivity(),
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });

                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                });
                dialogBuilder.create().show();
            }
        });


        //Get user id
        userService = new UserService();
        userService.getUserByEmail(new UserService.getUser() {
            @Override
            public void getUser(User user) {
                if(user != null) {
                    idUser = user.getId();

                    countFavorite();
                    countBill();
                    userService.getBillById(idUser, new UserService.HistoryBillReceivedListener() {
                        @Override
                        public void onSuccess(ArrayList<ListBill> listBill) {
                            adapterContainer = new HistoryBillAdapter(listBill);
                            containerContent.setAdapter(adapterContainer);
                            TextView history = binding.tvTickets;
                            history.setText(String.valueOf(listBill.size()));
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("Get User Error:", errorMessage);

            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Get User Error:", e.getMessage());
            }
        });

        //Config
        containerContent = binding.containerInfo;
        containerContent.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        historyBtn = binding.historyBtn;
        favBtn = binding.favoriteFilmBtn;
        pointBtn = binding.pointBtn;
        //Get click to change content and change background
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHistoryBtnClicked) {
                    isHistoryBtnClicked = false;
                } else {
                    isHistoryBtnClicked = true;
                    Toast.makeText(requireContext(), "Your Bill", Toast.LENGTH_SHORT).show();
                    userService.getBillById(idUser, new UserService.HistoryBillReceivedListener() {
                        @Override
                        public void onSuccess(ArrayList<ListBill> listBill) {
                            adapterContainer = new HistoryBillAdapter(listBill);
                            containerContent.setAdapter(adapterContainer);
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });

                }
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavBtnClicked){
                    isFavBtnClicked = false;
                }
                else {
                    isFavBtnClicked = true;
                    Toast.makeText(requireContext(), "Your favorites film", Toast.LENGTH_SHORT).show();

                    filmService.getFavoriteList(idUser, new FilmService.OnFilmFavoriteFrequentDataReceivedListener() {
                        @Override
                        public void onSuccess(ArrayList<ListFilmFavorite> listFavoriteFilm) {
                            adapterContainer = new FavoriteFilmAdapter(listFavoriteFilm);
                            containerContent.setAdapter(adapterContainer);
                        }

                        @Override
                        public void onError(String errorMessage) {

                        }
                    });
                }
            }
        });

        pointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPointBtnClicked){
                    isPointBtnClicked = false;
                }
                else {
                    isPointBtnClicked = true;
                }
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

    public void countBill(){
        userService.getBillById(idUser, new UserService.HistoryBillReceivedListener() {
            @Override
            public void onSuccess(ArrayList<ListBill> listBill) {
                bills = listBill.size();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTickets.setText(String.valueOf(bills));
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    public void countFavorite(){
        filmService.getFavoriteList(idUser, new FilmService.OnFilmFavoriteFrequentDataReceivedListener() {
            @Override
            public void onSuccess(ArrayList<ListFilmFavorite> listFavoriteFilm) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        favorites = listFavoriteFilm.size();
                        tvFavorite.setText(String.valueOf(favorites));
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
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
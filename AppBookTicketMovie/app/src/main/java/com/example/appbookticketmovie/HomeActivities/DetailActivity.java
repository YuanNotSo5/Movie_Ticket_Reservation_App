package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.appbookticketmovie.Adapter.ActorsListAdapter;
import com.example.appbookticketmovie.Adapter.CategoryListAdapter;
import com.example.appbookticketmovie.Adapter.CommentAdapter;
import com.example.appbookticketmovie.MainActivity;
import com.example.appbookticketmovie.Models.CommentItem;
import com.example.appbookticketmovie.Models.FilmItem;
import com.example.appbookticketmovie.Models.GenreItem;
import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.AlarmReceiver;
import com.example.appbookticketmovie.Services.FilmService;
import com.example.appbookticketmovie.Services.UserService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    //Cần ID user
    private long idUser = -1;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar progressBar;
    private TextView titleTxt, movieRateTxt, movieTimeTxt, movieSumInfo, showCmtTxt;
    private long idFilm;
    private ImageView pic2, backImg, isFavoriteImg;
    private RecyclerView.Adapter adapterActorList, adapterCategory, adapterComment;
    private RecyclerView recyclerViewActors, recyclerViewCategory, recyclerViewComment;
    private NestedScrollView scrollView;
    private WebView trailerContainer;

    private UserService userService;
    private Button bookTicketBtn, shareBtn;

    //Bottom Sheet Dilog
    BottomSheetDialog dialog;
    ProgressDialog pd;
    boolean isFavorite = false;
    ArrayList<CommentItem> listComment;
    String userName, avatar, idDocumentItem;
    private boolean isShow;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;
    private String nameFilm, originTime;
    private int hour = 0, minute = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();

        int tmp = getIntent().getIntExtra("id",0);
        isShow = getIntent().getBooleanExtra("isShow", true);
        idFilm = Long.valueOf(String.valueOf(tmp));
        initView();

        //Notification
        calendar = Calendar.getInstance();
        hour = getIntent().getIntExtra("hour", 0);
        minute = getIntent().getIntExtra("minute", 0);
        int year = getIntent().getIntExtra("year", 0);
        int month = getIntent().getIntExtra("month", 0);
        int day = getIntent().getIntExtra("day", 0);
        nameFilm = getIntent().getStringExtra("name");
        originTime = getIntent().getStringExtra("originTime");
        month = month - 1;
        calendar.set(year, month, day, hour, minute, 0);
        Log.d("Hour", String.valueOf(hour));
        Log.d("Minute", String.valueOf(minute));
        createNotificationChanel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(calendar.getTime());
        Log.d("CalendarLog", "Formatted Date: " + formattedDate);
        Log.d("NameFilm", "Formatted Date: " + nameFilm);

        if(nameFilm!=null){
            setAlarm();
        }

        Uri uri = getIntent().getData();
        if(uri!=null){
            List<String> params = uri.getPathSegments();
            String id = params.get(params.size()-2);
            String isShowString = params.get(params.size()-1);
            idFilm = Long.parseLong(id);
            isShow = Boolean.parseBoolean(isShowString);
        }
        if (!isShow) {
            bookTicketBtn.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) shareBtn.getLayoutParams();
            params.weight = 2;
            shareBtn.setLayoutParams(params);
            shareBtn.setPadding(450, 0, 0, 0);
        }
        sendRequest();

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp(DetailActivity.this, idFilm);
            }
        });

        //Bottom Sheet Dialog: https://www.youtube.com/watch?v=hclp2377fDQ
        dialog = new BottomSheetDialog(this);
        showCmtTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        userService = new UserService();
        listComment = new ArrayList<>();

        bookTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idUser == -1){
                    checkAuthenticationDialog();
                }
                else{
                    Intent chooseCinema = new Intent(DetailActivity.this, ChooseCinemaActivity.class);
                    chooseCinema.putExtra("idFilm", idFilm);
                    startActivity(chooseCinema);
                }
            }
        });

        userService = new UserService();
        userService.getUserByEmail(new UserService.getUser() {
            @Override
            public void getUser(User user) {
                if(user != null) {
                    idUser = user.getId();
                    getFavorite();
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

        isFavoriteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idUser == -1){
                    checkAuthenticationDialog();
                }
                else{
                    isFavorite = !isFavorite;
                    if (isFavorite) {
                        isFavoriteImg.setImageResource(R.drawable.baseline_favorite_24);

                    } else {
                        isFavoriteImg.setImageResource(R.drawable.baseline_favorite_24_white);
                    }
                    updateFavoriteFilm(isFavorite);
                }

            }
        });

    }

    private void updateFavoriteFilm(boolean isFavorite) {
        if(idDocumentItem==""){
            userService.addNewFavFilm(idFilm, idUser, new UserService.FavoriteReceivedListener() {
                @Override
                public void onSuccess(boolean isFavoriteListener, String idDocument) {
                    idDocumentItem = idDocument;
                    Toast.makeText(DetailActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        }
        else{
            userService.updateStateFavFilm(isFavorite, idDocumentItem, new UserService.FavoriteReceivedListener() {
                @Override
                public void onSuccess(boolean isFavoriteListener, String idDocument) {
                    Toast.makeText(DetailActivity.this, "SuccessUpdate", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String errorMessage) {

                }
            });
        }


    }

//    private void setAlarm(){
//        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, AlarmReceiver.class);
//        String content = "Phim "+nameFilm+" vào lúc: "+ String.valueOf(hour) + ":" + String.valueOf(minute);
//        intent.putExtra("notificationContent",content);
//        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
//    }


    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        String content = "Phim " + nameFilm + " vào lúc: " + originTime;
        intent.putExtra("notificationContent", content);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private void createNotificationChanel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "CNN-Reminder";
            String description = "Chanel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("cnn",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager!=null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void checkAuthenticationDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DetailActivity.this);
        dialogBuilder.setTitle("You are not logged in");
        dialogBuilder.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent login = new Intent(DetailActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
        dialogBuilder.setNegativeButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent register = new Intent(DetailActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });
        dialogBuilder.create().show();
    }

    private void createDialog() {

        BottomSheetBehavior <View> bottomSheetBehavior;
        View bottomSheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null, false);
        dialog.setContentView(bottomSheetView);

        //Sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        //Set to behaviour to expanded and minium height to parent layout
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //set min height to parent view
        CoordinatorLayout layout = dialog.findViewById(R.id.bottomSheetLayout);
        assert layout!=null;
        layout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        dialog.show();

        LinearLayout cmtSection = dialog.findViewById(R.id.cmtSection);
        if(idUser == -1){
            cmtSection.setVisibility(View.GONE);
        }

        //Close the dialog
        ImageView cancelBtn = dialog.findViewById(R.id.cancelButton);
        assert cancelBtn !=null;
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(bottomSheetBehavior.STATE_HIDDEN);
                recyclerViewComment.setAdapter(adapterComment);
                adapterComment.notifyDataSetChanged();
            }
        });

        RecyclerView recyclerViewComments = dialog.findViewById(R.id.commentContainer);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(adapterComment);
        //Pic User

        //Pic User
        ImageView userPhoto = dialog.findViewById(R.id.cmtPhoto);
        userService.getUser(idUser, new UserService.userInfoListener() {
            @Override
            public void onUserDataReceived(User userInfo) {
                avatar = userInfo.getAvatar();
                Glide.with(DetailActivity.this)
                        .load(userInfo.getAvatar())
                        .into(userPhoto);
                userName = userInfo.getUsername();
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("Error while display avatar", errorMessage);
            }
        });


        //Send new comment
        TextInputEditText inputCmt = dialog.findViewById(R.id.cmtInput);
        TextInputLayout ipLCmt = dialog.findViewById(R.id.inputLayoutCmt);
        ImageView sendBtn = dialog.findViewById(R.id.sendBtn);
        RatingBar rate = dialog.findViewById(R.id.ratingBarUser);
        pd = new ProgressDialog(this);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputCmt.getText().toString().isEmpty()){
                    ipLCmt.setHelperText("");
                    ipLCmt.setError("How is your experiences");
                }else{
                    UserService user = new UserService();
                    pd.setTitle("Add new comment");
                    pd.show();
                    ipLCmt.setHelperText("");
                    ipLCmt.setError("");
                    user.postComment(userName, avatar, String.valueOf(inputCmt.getText()), idUser, idFilm, String.valueOf(rate.getRating()), new UserService.OnCmtDataReceivedListener() {
                        @Override
                        public void onCmtDataReceived(CommentItem newCmt) {
                            pd.dismiss();
                            inputCmt.setText("");
                            rate.setRating(0.0F);
                            listComment.add(newCmt);
                            Log.d("GAOOOOOOOO", String.valueOf(listComment));
                            if (adapterComment == null) {
                                adapterComment = new CommentAdapter(listComment);
                                recyclerViewComments.setAdapter(adapterComment);
                            }

                            adapterComment.notifyDataSetChanged();
                        }
                        @Override
                        public void onError(String errorMessage) {
                            pd.dismiss();
                            Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    public void shareApp(Context context, Long id)
    {
        final String appPackageName = context.getPackageName();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Wanna Watch Some Nice Things?: http://www.cnnmovieticket.com/details/" + String.valueOf(id)+"/"+String.valueOf(isShow));
        sendIntent.setType("text/plain");
        if(sendIntent.resolveActivity(getPackageManager())!=null){
            context.startActivity(sendIntent);
        }
    }

    private void getFavorite(){
        UserService userService = new UserService();
        userService.getFavorite(idUser, idFilm, new UserService.FavoriteReceivedListener() {


            @Override
            public void onSuccess(boolean isFavoriteListener, String idDocument) {

                isFavorite = isFavoriteListener;
                idDocumentItem = idDocument;
                if(isFavoriteListener){
                    isFavoriteImg.setImageResource(R.drawable.baseline_favorite_24);
                } else {
                    isFavoriteImg.setImageResource(R.drawable.baseline_favorite_24_white);
                }
            }

            @Override
            public void onError(String errorMessage) {
                isFavoriteImg.setImageResource(R.drawable.baseline_favorite_24_white);
                isFavorite = false;
            }
        });
    }

    private void sendRequest(){
        //Film
        FilmService film = new FilmService();

        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        film.getFilmById(idFilm, new FilmService.OnFilmDataReceivedListener() {
            @Override
            public void onFilmDataReceived(ArrayList<FilmItem> listFilms) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                FilmItem item = listFilms.get(0);
                Glide.with(DetailActivity.this)
                        .load(item.getPoster())
                        .into(pic2);
                titleTxt.setText(item.getTitle());
                movieRateTxt.setText(item.getImdbRating());
                movieTimeTxt.setText(item.getRuntime());
                movieSumInfo.setText(item.getPlot());

                String videoLink = item.getTrailer();
                String video =
                        "<iframe width=\"100%\" " +
                                "height=\"100%\" " +
                                "src=\""+videoLink+"\" " +
                                "title=\"YouTube video player\" frameborder=\"0\" " +
                                "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
                trailerContainer.loadData(video, "text/html", "utf-8");
                trailerContainer.getSettings().setJavaScriptEnabled(true);
                trailerContainer.getSettings().setLoadWithOverviewMode(true);
                trailerContainer.getSettings().setUseWideViewPort(true);
                trailerContainer.getSettings().setDomStorageEnabled(true);

                trailerContainer.loadData(video, "text/html", "utf-8");
                trailerContainer.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        Log.e("WebView", "Error: " + error.getDescription());
                    }
                });

                if(item.getActors()!=null){
                    adapterActorList=new ActorsListAdapter(item.getActors());
                    recyclerViewActors.setAdapter(adapterActorList);
                }
                if(item.getGenres()!=null){
                    adapterCategory=new CategoryListAdapter(item.getGenres());
                    recyclerViewCategory.setAdapter(adapterCategory);
                }



                //Load Cmt
                userService.LoadAllCmtOfFilm(idFilm, new UserService.CmtListListener() {
                    @Override
                    public void onCmtDataReceived(ArrayList<CommentItem> listCommentsOfFilm) {
                        listComment = listCommentsOfFilm;
                        adapterComment = new CommentAdapter(listComment);
                        recyclerViewComment.setAdapter(adapterComment);
                    }
                    @Override
                    public void onError(String errorMessage) {

                    }
                });

            }
            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
            }
        });



    }

//    private void sendRequest(){
//        mRequestQueue = Volley.newRequestQueue(this);
//        progressBar.setVisibility(View.VISIBLE);
//        scrollView.setVisibility(View.GONE);
//        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/m-4YnwBwaGk?si=1YOO6k29uzkk4wSZ\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
//        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies/" + idFilm, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Gson gson = new Gson();
//                progressBar.setVisibility(View.GONE);
//                scrollView.setVisibility(View.VISIBLE);
//                FilmItem item = gson.fromJson(response, FilmItem.class);
//
//                Glide.with(DetailActivity.this)
//                        .load(item.getPoster())
//                        .into(pic2);
//                titleTxt.setText(item.getTitle());
//                movieRateTxt.setText(item.getImdbRating());
//                movieTimeTxt.setText(item.getRuntime());
//                movieSumInfo.setText(item.getPlot());
////                movieActorInfo.setText(item.getActors());
//                //Set trailer
//                trailerContainer.loadData(video, "text/html","uft-8");
//                trailerContainer.getSettings().setJavaScriptEnabled(true);
//                trailerContainer.setWebChromeClient(new WebChromeClient());
//
//
//                if(item.getImages()!=null){
//                    adapterActorList=new ActorsListAdapter(item.getImages());
//                    recyclerViewActors.setAdapter(adapterActorList);
//                }
//                if(item.getGenres()!=null){
//                    adapterCategory=new CategoryEachFilmListAdapter(item.getGenres());
//                    recyclerViewCategory.setAdapter(adapterCategory);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
//            }
//        });
//        mRequestQueue.add(mStringRequest);
//    }

    private void initView(){
        titleTxt = findViewById(R.id.movieNameTxt);
        progressBar = findViewById(R.id.progressBarDetail);
        scrollView = findViewById(R.id.scrollView2);
        pic2 = findViewById(R.id.detailPic);
        movieRateTxt = findViewById(R.id.movieStar);
        movieTimeTxt = findViewById(R.id.movieTime);
        movieSumInfo = findViewById(R.id.movieSummaryContextTxt);
        showCmtTxt = findViewById(R.id.showCommentListTxt);

        backImg = findViewById(R.id.backImg);
        isFavoriteImg = findViewById(R.id.isFavorite);
        recyclerViewCategory = findViewById(R.id.genreView);
        recyclerViewActors = findViewById(R.id.imagesRecycler);
        recyclerViewComment = findViewById(R.id.commentContainer);
        recyclerViewActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Btn
        bookTicketBtn = findViewById(R.id.bookTicketBtn);



        shareBtn = findViewById(R.id.sharingBtn);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });
        trailerContainer = findViewById(R.id.trailerContainer);
    }
    //https://fireship.io/lessons/firestore-nosql-data-modeling-by-example/
}
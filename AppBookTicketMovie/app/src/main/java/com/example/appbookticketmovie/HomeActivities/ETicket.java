package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.appbookticketmovie.Adapter.ETicketAdapter;
import com.example.appbookticketmovie.MainActivity;
import com.example.appbookticketmovie.Models.Ticket;
import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.Models.seatInfo;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.UUID;

public class ETicket extends AppCompatActivity {
    private RecyclerView.Adapter adapterTicket;
    public static final String CHANNEL_ID = "SuccessBuyingTicket";

    private RecyclerView recyclerViewTicket;
    private UserService userService;
    private ArrayList<Ticket> ticketInfo;
    private ArrayList<seatInfo> seatInfos;
    private long idUser = -1, idFilm;
    private User currUser;
    private int point;
    private String nameFilm, nameCinema, date, time, idRoom, addressCinema;
    private long total;
    private static final int NOTIFICATION_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket);
        init();
        getSupportActionBar().hide();
        createNotificationChannel();

        Intent book = getIntent();
        idFilm = book.getLongExtra("idFilm",0);
        nameFilm = book.getStringExtra("nameFilm");
        nameCinema = book.getStringExtra("nameCinema");
        addressCinema = book.getStringExtra("addressCinema");
        date = book.getStringExtra("date");
        time = book.getStringExtra("time");
        idRoom = book.getStringExtra("idRoom");
        total = book.getLongExtra("total", 0);
        point = book.getIntExtra("point",0);
        seatInfos = book.getParcelableArrayListExtra("bookseat");
        int paymentMethod = book.getIntExtra("paymentMethod", 0);

        sendNotification();
        Log.d("aaaaaaaaaaaaaaaaaaaa", time);
//        ActionBar actionBar = getSupportActionBar();

        ticketInfo = new ArrayList<>();
        for(seatInfo info : seatInfos){
            Ticket addTicket = new Ticket(idFilm, idUser, nameFilm, time, date, nameCinema, info.getSeat(), idRoom, info.getType(), info.getPriceDetail(), "");
            if(paymentMethod == 1) {
                addTicket.setPaymentMethod("CARD");
                addTicket.setPaymentStatus(true);
            }
            ticketInfo.add(addTicket);
        }
        // Tạo một Thread mới để thực hiện mua vé
        Thread buyTicketThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Lưu
                UserService userTicket = new UserService();
                userTicket.getUserByEmail(new UserService.getUser() {
                    @Override
                    public void getUser(User user) {
                        if(user != null){
                            idUser = user.getId();
                            currUser = user;
                            point = user.getPoint();

                            userTicket.buyTicket(ticketInfo, idUser);

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
            }
        });

        // Chạy luồng mua vé
        buyTicketThread.start();

        // Tạo một luồng mới để cập nhật UI (nếu cần thiết)
        Thread updateUIThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Chờ cho luồng mua vé hoàn thành trước khi cập nhật UI
                try {
                    buyTicketThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Cập nhật giao diện người dùng ở đây (nếu cần thiết)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createBarcode();
                        adapterTicket = new ETicketAdapter(ticketInfo);
                        recyclerViewTicket.setAdapter(adapterTicket);
                    }
                });
            }
        });

        // Chạy luồng cập nhật UI
        updateUIThread.start();
    }

    //Notification
    private void sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        String notificationContent = "Bạn đã mua thành công " + String.valueOf(seatInfos.size()) + " vé Phim " + nameFilm + ".\nThanh toán thành công: "+String.valueOf(total);
        //Cài đặt đtg notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("CNN - Cinema NNg")
                .setContentText(notificationContent)
                .setSmallIcon(R.drawable.avatar)
                .setLargeIcon(bitmap)
                .build();
        //Khai báo notification Manager
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ETicket.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.user_menu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.finish:
//                Intent intent = new Intent(ETicket.this, MainActivity.class);
//                startActivity(intent);
//
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void createBarcode() {
        for (Ticket item:ticketInfo) {
            String filmCode = item.getFilm()+"_"+item.getDate();
            String uuid = UUID.randomUUID().toString();
            String code = filmCode + "_" + uuid;

            MultiFormatWriter writer = new MultiFormatWriter();
            try{
                BitMatrix matrix = writer.encode(code, BarcodeFormat.CODE_128,50,170);
                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                String barcodeString = item.bitmapToString(bitmap);
                item.setBarcode(barcodeString);
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void init() {
        recyclerViewTicket = findViewById(R.id.ticketContainer);
        recyclerViewTicket.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }


    @Override
    public void onBackPressed() {
        // Chuyển sang trang DetailsActivity
        Intent detailsIntent = new Intent(this, DetailActivity.class);
        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        int tmp = (int) idFilm;
        detailsIntent.putExtra("id", tmp);
        detailsIntent.putExtra("isShow",true);
        String[] timeFilm = time.split("-");
        String []startTime = timeFilm[0].split(":");

        int hour = Integer.parseInt(startTime[0].trim());
        int minute = Integer.parseInt(startTime[1].trim());

        if (minute < 30) {
            minute = 60 - (30 - minute);
            hour = hour - 1;
            if (hour == 0) {
                hour = 11;
            }
        }else{
            minute = minute -30;
        }

        String []dateArr = date.split("-");
        int day = Integer.parseInt(dateArr[0].trim());
        int month = Integer.parseInt(dateArr[1].trim());
        int year = Integer.parseInt(dateArr[2].trim());
        detailsIntent.putExtra("hour", hour);
        detailsIntent.putExtra("minute",minute);
        detailsIntent.putExtra("originTime",timeFilm[0]);
        detailsIntent.putExtra("day",day);
        detailsIntent.putExtra("month",month);
        detailsIntent.putExtra("year",year);
        detailsIntent.putExtra("name", nameFilm);
        startActivity(detailsIntent);
        super.onBackPressed();
    }

}
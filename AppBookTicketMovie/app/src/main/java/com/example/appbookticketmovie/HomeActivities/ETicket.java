package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

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
    private RecyclerView recyclerViewTicket;
    private UserService userService;
    private ArrayList<Ticket> ticketInfo;
    private ArrayList<seatInfo> seatInfos;
    private long idUser = -1, idFilm;
    private User currUser;
    private int point;
    private String nameFilm, nameCinema, date, time, idRoom, addressCinema;
    private long total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket);
        init();
        getSupportActionBar().hide();
        Intent book = getIntent();
        idFilm = book.getLongExtra("idFilm",0);
        nameFilm = book.getStringExtra("nameFilm");
        nameCinema = book.getStringExtra("nameCinema");
        addressCinema = book.getStringExtra("addressCinema");
        date = book.getStringExtra("date");
        time = book.getStringExtra("time");
        idRoom = book.getStringExtra("idRoom");
        total = book.getLongExtra("total", 0);

        seatInfos = book.getParcelableArrayListExtra("bookseat");
        int paymentMethod = book.getIntExtra("paymentMethod", 0);

        ActionBar actionBar = getSupportActionBar();

        // Tôi cần nhận
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.finish:
                Intent intent = new Intent(ETicket.this, MainActivity.class);
                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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
        startActivity(detailsIntent);
        super.onBackPressed();
    }

}
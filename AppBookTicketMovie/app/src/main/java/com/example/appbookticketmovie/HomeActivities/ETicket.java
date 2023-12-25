package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.example.appbookticketmovie.Adapter.ActorsListAdapter;
import com.example.appbookticketmovie.Adapter.ETicketAdapter;
import com.example.appbookticketmovie.Models.Ticket;
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
    ArrayList<Ticket> ticketInfo;
    long idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket);
        init();
        idUser = 1L;

        // Tôi cần nhận
        ticketInfo = new ArrayList<>();
        ticketInfo.add(new Ticket(1L, "Film1", "12:00", "2023-01-01", "Cinema1", "A1", "Room1", "Standard", 10000,""));
        ticketInfo.add(new Ticket(2L,"Film2", "15:30", "2023-01-02", "Cinema2", "B3", "Room2", "VIP", 30000, ""));

        // Tạo một Thread mới để thực hiện mua vé
        Thread buyTicketThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Lưu
                UserService userTicket = new UserService();
                userTicket.buyTicket(ticketInfo, idUser);
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

}
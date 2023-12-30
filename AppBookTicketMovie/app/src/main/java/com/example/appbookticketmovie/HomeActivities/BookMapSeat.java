package com.example.appbookticketmovie.HomeActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbookticketmovie.Models.Room;
import com.example.appbookticketmovie.Models.seatInfo;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.CinemaService;

import java.util.ArrayList;
import java.util.List;

public class BookMapSeat extends AppCompatActivity  implements View.OnClickListener{

    ViewGroup layout;
    TextView movieTicketInfo;
    char[] charMap;


    //V là ghế vip
    //A ghế thường
    //R đã đặt
//    String seats = "_UUUUUUAAAAARRRR_/"
//            + "_________________/"
//            + "UU__AAAARRRRR__RR/"
//            + "UU__UUUAAAAAA__AA/"
//            + "AA__AAAAAAAAA__AA/"
//            + "AA__AARUUUURR__AA/"
//            + "UU__UUUA_RRRR__AA/"
//            + "AA__AAAA_RRAA__UU/"
//            + "AA__AARR_UUUU__RR/"
//            + "AA__UUAA_UURR__RR/"
//            + "_________________/"
//            + "UU_AAAAAAAUUUU_RR/"
//            + "RR_AAAAAAAAAAA_AA/"
//            + "AA_UUAAAAAUUUU_AA/"
//            + "AA_AAAAAAUUUUU_AA/"
//            + "_________________/";
    String seats = "";
    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 100;
    int seatGaping = 10;
    int STATUS_AVAILABLE = 1;
    int STATUS_VIP = 2;
    int STATUS_RESERVED = 3;

    long VIP_PRICE = 30000;
    long NORMAL_PRICE = 10000;
    String selectedIds = "";
    String idRoom, nameFilm, time, date, nameCinema, addressCinema;
    int idCinema;
    long price, idFilm, total = 0;

    Button btnConfirm;
    TextView totalAmount, filmInfo;
    ArrayList<seatInfo> seatInfoList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_map_seat);


        //Element
        btnConfirm = findViewById(R.id.btnBookTicket);
        totalAmount = findViewById(R.id.totalAmount);
        movieTicketInfo = findViewById(R.id.movieTicketInfo);

        //Get Map: Cần nhận
        idRoom = getIntent().getStringExtra("idRoom");
        idCinema = getIntent().getIntExtra("idCinema",1);
        price = getIntent().getLongExtra("price",0);
        nameFilm = getIntent().getStringExtra("nameFilm");
        date = getIntent().getStringExtra("date");
        time = getIntent().getStringExtra("time");
        nameCinema = getIntent().getStringExtra("nameCinema");
        addressCinema = getIntent().getStringExtra("addressCinema");
        idFilm = getIntent().getLongExtra("idFilm",0);

        Log.d("idFilm2", String.valueOf(idFilm));
//        idRoom = "A";
//        price = 45000;

        movieTicketInfo.setText(nameFilm);

        CinemaService seat = new CinemaService();
        seat.getMapRoom(idRoom, idCinema, new CinemaService.OnCinemaDataReceivedListener2() {
            @Override
            public void onCinemaDataReceived(Room room) {
                seats = room.getMap();
                charMap = seats.toCharArray();
                processSeatData();
            }
            @Override
            public void onError(String errorMessage) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookMapSeat.this, TicketBookActivity.class);
                intent.putExtra("nameFilm", nameFilm);
                intent.putExtra("nameCinema", nameCinema);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("total", Long.valueOf(totalAmount.getText().toString()));
                intent.putExtra("idRoom", idRoom);
                intent.putExtra("addressCinema", addressCinema);
                intent.putExtra("newMap",new String(charMap));
                intent.putExtra("idCinema", idCinema);
                intent.putExtra("idFilm", idFilm);
                intent.putParcelableArrayListExtra("bookseat", (ArrayList<? extends Parcelable>) seatInfoList);
                startActivity(intent);
            }
        });
    }

    private void processSeatData() {
        layout = findViewById(R.id.layoutSeat);
        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        layout.addView(layoutSeat);

        LinearLayout layout = null;
        int count = 0;
        for (int index = 0; index < seats.length(); index++) {
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seats.charAt(index) == 'V') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_booked);
                view.setTextColor(Color.WHITE);

                int seatStatus = STATUS_VIP;
                String tagValue = seatStatus + "_" + index;
                view.setTag(tagValue);

                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_book);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                int seatStatus = STATUS_AVAILABLE;
                String tagValue = seatStatus + "_" + index;
                view.setTag(tagValue);

                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'R') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_reserved);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.WHITE);
                int seatStatus = STATUS_RESERVED;
                String tagValue = seatStatus + "_" + index;
                view.setTag(tagValue);

                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }
    }

    @Override
    public void onClick(View view) {
        String tag = view.getTag().toString();
        String[] idSeat = tag.split("_");
        int index = Integer.parseInt(idSeat[1]);
        int status = Integer.parseInt(idSeat[0]);

        if ((int) status == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.getId() + ",")) {
                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_book);
                total = total - (price + NORMAL_PRICE);
                totalAmount.setText(String.valueOf(total));
                charMap[index] = 'A';
                removeSeatInfo(view.getId());
            } else {
                selectedIds = selectedIds + view.getId() + ",";
                view.setBackgroundResource(R.drawable.ic_seats_selected);
                total = total + (price + NORMAL_PRICE);
                totalAmount.setText(String.valueOf(total));
                charMap[index] = 'R';
                addSeatInfo(new seatInfo(String.valueOf(view.getId()), "Normal", price + NORMAL_PRICE));
            }
        } else if (status == STATUS_VIP) {
            if (selectedIds.contains(view.getId() + ",")) {
                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_book);
                total = total - (price + VIP_PRICE);
                totalAmount.setText(String.valueOf(total));
                charMap[index] = 'V';
                removeSeatInfo(view.getId());
            } else {
                selectedIds = selectedIds + view.getId() + ",";
                view.setBackgroundResource(R.drawable.ic_seats_selected);
                total = total + (price + VIP_PRICE);
                charMap[index] = 'R';

                totalAmount.setText(String.valueOf(total));

                addSeatInfo(new seatInfo(String.valueOf(view.getId()), "VIP", price + VIP_PRICE));
            }
        } else if ((int) status == STATUS_RESERVED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Reserved", Toast.LENGTH_SHORT).show();
        }
    }

    private void addSeatInfo(seatInfo info) {
        if (seatInfoList == null) {
            seatInfoList = new ArrayList<>();
        }

        seatInfoList.add(info);
    }

    private void removeSeatInfo(int seatId) {
        if (seatInfoList != null) {
            for (seatInfo info : seatInfoList) {
                if (info.getSeat().equals(String.valueOf(seatId))) {
                    seatInfoList.remove(info);
                    break;
                }
            }
        }
    }
//    public class seatInfo {
//        private String seat;
//        private String type;
//        private long priceDetail;
//
//        public seatInfo(String seat, String type, long priceDetail) {
//            this.seat = seat;
//            this.type = type;
//            this.priceDetail = priceDetail;
//        }
//
//        public String getSeat() {
//            return seat;
//        }
//
//        public void setSeat(String seat) {
//            this.seat = seat;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public long getPriceDetail() {
//            return priceDetail;
//        }
//
//        public void setPriceDetail(long priceDetail) {
//            this.priceDetail = priceDetail;
//        }
//
//        @Override
//        public String toString() {
//            return "seatInfo{" +
//                    "seat='" + seat + '\'' +
//                    ", type='" + type + '\'' +
//                    ", priceDetail=" + priceDetail +
//                    '}';
//        }
//    }
}


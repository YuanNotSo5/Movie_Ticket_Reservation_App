package com.example.appbookticketmovie.HomeActivities;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbookticketmovie.Adapter.CinemaListAdapter;
import com.example.appbookticketmovie.Adapter.InvoiceInfoAdapter;
import com.example.appbookticketmovie.HomeActivities.ChooseCinemaActivity;
import com.example.appbookticketmovie.Models.Room;
import com.example.appbookticketmovie.Models.User;
import com.example.appbookticketmovie.Models.seatInfo;
import com.example.appbookticketmovie.R;
import com.example.appbookticketmovie.Services.CinemaService;
import com.example.appbookticketmovie.Services.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TicketBookActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 1;
    private ArrayList<seatInfo> seatInfos;
    private InvoiceInfoAdapter invoiceInfoAdapter;
    private UserService userService;
    private int pay = -1;
    private User currUser;
    private String nameFilm, nameCinema, date, time, idRoom, addressCinema, newMap;
    private int idCinema, userPoint, pointInUser = 0, newPoint, plusPoint=0;
    private long total, newTotal, idFilm;
    RecyclerView recyclerView;
    LinearLayout paymentSection;
    Button cardPaymentBtn, checkOutBtn, cashPaymentBtn;
    TextInputEditText cardName, cardNumber;
    TextView tvPrice, tvOldPrice, tvPoint, tvPointInUse;
    CheckBox chkbxPoint;
    PaymentButtonContainer paymentButtonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_book);
        getSupportActionBar().hide();
        Intent book = getIntent();
        idFilm = book.getLongExtra("idFilm",0);
        nameFilm = book.getStringExtra("nameFilm");
        nameCinema = book.getStringExtra("nameCinema");
        addressCinema = book.getStringExtra("addressCinema");
        date = book.getStringExtra("date");
        time = book.getStringExtra("time");
        idRoom = book.getStringExtra("idRoom");
        total = book.getLongExtra("total",0);
        seatInfos = book.getParcelableArrayListExtra("bookseat");
        idCinema = book.getIntExtra("idCinema",0);
        newMap = book.getStringExtra("newMap");


        userService = new UserService();

        cardPaymentBtn = findViewById(R.id.button2);
        paymentSection = findViewById(R.id.card_payment_section);
        checkOutBtn = findViewById(R.id.button5);
        cashPaymentBtn = findViewById(R.id.button3);
        cardName = findViewById(R.id.cardName);
        cardNumber = findViewById(R.id.cardNumber);
        tvPrice = findViewById(R.id.tvPrice);
        tvOldPrice = findViewById(R.id.tvOldPrice);
        chkbxPoint = findViewById(R.id.checkBoxPoint);
        paymentButtonContainer = findViewById(R.id.payment_button_container);
        tvPoint = findViewById(R.id.tvPoint);
        recyclerView = findViewById(R.id.recyclerViewTicketInvoice);
        tvPointInUse = findViewById(R.id.tvPointInUse);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        invoiceInfoAdapter = new InvoiceInfoAdapter(this, seatInfos, book);
        recyclerView.setAdapter(invoiceInfoAdapter);
        tvPrice.setText(String.valueOf(total) + "VND");
        tvOldPrice.setText(String.valueOf(total) + "VND");
        newTotal = total;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(tvOldPrice.getText().toString());
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableStringBuilder.setSpan(strikethroughSpan, 0, tvOldPrice.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvOldPrice.setText(spannableStringBuilder);

        getPoint();
        updatePoint();
//        getCard();



        cardPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentSection.setVisibility(View.VISIBLE);
                checkOutBtn.setVisibility(View.VISIBLE);
                pay = 1;
            }
        });

        cashPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutBtn.setVisibility(View.VISIBLE);
                paymentSection.setVisibility(GONE);
                pay = 0;
            }
        });

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, number;
                boolean status = true;
                if(pay == 1){
                    System.out.println("Payment Method: " + pay);
                    name = cardName.getText().toString();
                    number = cardNumber.getText().toString();
                    if(name.equals("")){
                        cardName.setError("Please Enter Your Card Name");
                        cardName.setEnabled(true);
                        status = false;
                    }
                    if(number.equals("")){
                        cardNumber.setError("Please Enter Your Card Number");
                        cardNumber.setEnabled(true);
                        status = false;
                    }
                    if(status) {
                        cardName.setEnabled(false);
                        cardNumber.setEnabled(false);

                        Map<String, String> newCard = new HashMap<>();
                        newCard.put("name", name);
                        newCard.put("number", number);

                        userService.updateCard(newCard, new UserService.cardUpdate() {
                            @Override
                            public void updateCard(boolean status) {
                                if (status) {
                                    Toast.makeText(TicketBookActivity.this, "Update New Card Successfully", Toast.LENGTH_SHORT).show();
                                    getETicketAct();
                                }
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.d("Update Card Error", e.getMessage());
                            }
                        });
                    }
                }else{
                    getETicketAct();
                }
            }
        });
        //Paypal
        paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        double dollar = newTotal / 23000.0;
                        BigDecimal bd = new BigDecimal(dollar);
                        bd = bd.setScale(2, RoundingMode.HALF_UP);
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(String.valueOf(bd))
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                Log.i("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                Toast.makeText(TicketBookActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                getETicketAct();

                            }
                        });
                    }
                }
        );

        //Discount
        chkbxPoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    tvOldPrice.setVisibility(View.VISIBLE);

                    pointInUser = userPoint/10;
                    if(pointInUser>50){
                        pointInUser = 50;
                    }
                    newTotal = (long) (total - (float)(total*(pointInUser*(2/100.0))));
                    tvPrice.setText(String.valueOf(newTotal));

                }
                else{
                    pointInUser = 0;
                    tvOldPrice.setVisibility(GONE);
                    tvPrice.setText(String.valueOf(total));
                    newTotal = total;
                }
                updatePoint();
                tvPointInUse.setText(String.valueOf(pointInUser)+"0");
            }
        });


    }

    private void updatePoint() {

         newPoint = (userPoint - pointInUser*10);
         plusPoint = (int) (newTotal/100000);
         newPoint = newPoint + plusPoint;
         tvPoint.setText(String.valueOf(plusPoint));
    }

    private void getPoint() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                userService.getUserByEmail(new UserService.getUser() {
                    @Override
                    public void getUser(User user) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                userPoint = user.getPoint();
                                chkbxPoint.setText(String.valueOf(user.getPoint()));
                                if(userPoint<10){
                                    chkbxPoint.setVisibility(GONE);
                                    tvPointInUse.setVisibility(GONE);

                                }
                                else{
                                    chkbxPoint.setVisibility(View.VISIBLE);
                                    tvPointInUse.setVisibility(View.VISIBLE);
                                }
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



    public void getETicketAct(){
        CinemaService updateMap = new CinemaService();
        if(pay!=1){
            newPoint = newPoint - plusPoint;
        }
        updateMap.updateSeatMap(newMap, idCinema, idRoom, new CinemaService.OnCinemaDataReceivedListener2() {
            @Override
            public void onCinemaDataReceived(Room room) {

                userService.updatePoint(newPoint, new UserService.pointUpdate() {
                    @Override
                    public void updatePoint(boolean status) {
                        Intent checkout = new Intent(TicketBookActivity.this, ETicket.class);
                        checkout.putExtra("idFilm", idFilm);
                        checkout.putExtra("paymentMethod", pay);
                        checkout.putExtra("nameFilm", nameFilm);
                        checkout.putExtra("nameCinema", nameCinema);
                        checkout.putExtra("addressCinema", addressCinema);
                        checkout.putExtra("date", date);
                        checkout.putExtra("time", time);
                        checkout.putExtra("idRoom", idRoom);
                        checkout.putExtra("total", newTotal);
                        checkout.putExtra("point", plusPoint);
                        checkout.putParcelableArrayListExtra("bookseat", (ArrayList<? extends Parcelable>) seatInfos);

                        startActivity(checkout);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("Update Point", e.getMessage());
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

//    private void sendNotification(){
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background);
//        //Cài đặt đtg notification
//        Notification notification = new Notification.Builder(this)
//                .setContentTitle("CNN - Cinema NNg")
//                .setContentText("This is success buy ticket")
//                .setSmallIcon(R.drawable.avatar)
//                .setLargeIcon(bitmap)
//                .build();
//        //Khai báo notification Manager
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if(notificationManager!=null){
//            notificationManager.notify(NOTIFICATION_ID, notification);
//        }
//
//    }


    public void getCard(){
        userService.getUserByEmail(new UserService.getUser() {
            @Override
            public void getUser(User user) {
                if(user != null){
                    currUser = user;
                    Map<String, String> card = user.getCard();
                    if(card.get("name") != null && card.get("number") != null){
                        cardName.setText(card.get("name"));
                        cardNumber.setText(card.get("number"));
                    }
                }
            }

            @Override
            public void onError(String errorMessage) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

}
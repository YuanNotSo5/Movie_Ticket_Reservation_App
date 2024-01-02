package com.example.appbookticketmovie.Models;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.pyplcheckout.BuildConfig;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PayPalCheckout.setConfig(new CheckoutConfig(
                this,
                "AbsFE5jXO7oDzzMOBUPj57K8KWI0BBr-y4fJXRTJuUbEJIo9vmIPNL9_t1M9nj3NF94Zt6lmGxvKB8ZZ",
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                "com.example.appbookticketmovie://paypalpay"
        ));
    }
}

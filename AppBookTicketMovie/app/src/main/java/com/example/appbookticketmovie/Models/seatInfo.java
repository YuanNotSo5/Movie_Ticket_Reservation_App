package com.example.appbookticketmovie.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class seatInfo  implements Parcelable {
    private String seat;
    private String type;
    private long priceDetail;

    public seatInfo(String seat, String type, long priceDetail) {
        this.seat = seat;
        this.type = type;
        this.priceDetail = priceDetail;
    }

    protected seatInfo(Parcel in) {
        seat = in.readString();
        type = in.readString();
        priceDetail = in.readLong();
    }

    public static final Creator<seatInfo> CREATOR = new Creator<seatInfo>() {
        @Override
        public seatInfo createFromParcel(Parcel in) {
            return new seatInfo(in);
        }

        @Override
        public seatInfo[] newArray(int size) {
            return new seatInfo[size];
        }
    };

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPriceDetail() {
        return priceDetail;
    }

    public void setPriceDetail(long priceDetail) {
        this.priceDetail = priceDetail;
    }

    @Override
    public String toString() {
        return "seatInfo{" +
                "seat='" + seat + '\'' +
                ", type='" + type + '\'' +
                ", priceDetail=" + priceDetail +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(seat);
        parcel.writeString(type);
        parcel.writeLong(priceDetail);
    }
}
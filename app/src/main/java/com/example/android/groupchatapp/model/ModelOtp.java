package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

public class ModelOtp {

    @SerializedName("otp")
    private int otp;

    public ModelOtp(int Otp){
        this.otp=Otp;
    }

    public int getOtp(){
        return otp;
    }
}

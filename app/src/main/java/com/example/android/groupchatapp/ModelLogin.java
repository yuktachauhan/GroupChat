package com.example.android.groupchatapp;

import com.google.gson.annotations.SerializedName;

public class ModelLogin {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public ModelLogin(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}

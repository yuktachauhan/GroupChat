package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

public class ModelToken {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("token")
    private String Token;

    public ModelToken(){

    }

    public ModelToken(String username,String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getToken(){
        return Token;
    }}

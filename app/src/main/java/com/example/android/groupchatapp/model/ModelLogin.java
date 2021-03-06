package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

public class ModelLogin {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("user_id")
    private int user_id;


    public ModelLogin(String username,String password){
        this.username=username;
        this.password=password;
    }

    public ModelLogin(){           //empty constructor

    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public int getUser_id(){
        return user_id;
    }
}

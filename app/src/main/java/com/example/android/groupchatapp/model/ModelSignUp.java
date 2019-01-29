package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

public class ModelSignUp {

    @SerializedName("email")
    private String email;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("confirm_password")
    private String confirm_password;

    public ModelSignUp(){}

    public ModelSignUp(String email,String username,String password,String confirm_password){
        this.email=email;
        this.username=username;
        this.password=password;
        this.confirm_password=confirm_password;
    }

    public String getEmail(){
        return email;
    }
     public String getUsername(){
        return username;
     }
     public String getPassword(){
        return password;
     }
     public String getConfirm_password(){
        return confirm_password;
     }
}

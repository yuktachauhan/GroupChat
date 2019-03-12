package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class ModelProfile {
    @SerializedName("name")
    private String name;


    @SerializedName("phone_number")
    private String phone_number;

    @SerializedName("avatar")
    private String avatar;

   @SerializedName("admin")
    private String admin;

    public ModelProfile(String name,String phone_number){
        this.name =name;
        this.phone_number=phone_number;
    }


    public ModelProfile(){         //empty constructor

    }

    public String getName(){
        return name;
    }

    public String getPhone_number(){
        return  phone_number;
    }

    public String getAvatar(){
        return avatar;
    }

    public String getAdmin(){return admin;}
}

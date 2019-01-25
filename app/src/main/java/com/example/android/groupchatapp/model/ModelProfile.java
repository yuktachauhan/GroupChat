package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

public class ModelProfile {
    @SerializedName("name")
    private String name;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("phone_number")
    private String phone_number;

    public ModelProfile(String name,String avatar,String phone_number){
        this.name =name;
        this.avatar=avatar;
        this.phone_number=phone_number;
    }
    public ModelProfile(String name,String phone_number){
        this.name =name;

        this.phone_number=phone_number;
    }


    public String getName(){
        return name;
    }

    public String getAvatar(){
        return  avatar;
    }

    public String getPhone_number(){
        return  phone_number;
    }
}

package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ModelMemberAdd {

    @SerializedName("member_data")
    private HashMap<String,String> memberData;

    public ModelMemberAdd(HashMap<String,String> hashMap){
        memberData=hashMap;
    }
}

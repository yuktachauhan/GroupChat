package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelAddMemberListResponse {
    @SerializedName("member_list")
    private ArrayList<HashMap<String,String>> member_list;

    @SerializedName("member_data")
    private HashMap<String,String> memberData;


    public ArrayList<HashMap<String,String>> getMember_list(){
        return member_list;
    }

    public ModelAddMemberListResponse(HashMap<String,String> hashMap){
        memberData=hashMap;
    }
}

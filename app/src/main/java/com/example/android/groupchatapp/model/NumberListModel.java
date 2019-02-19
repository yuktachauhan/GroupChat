package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class NumberListModel {

    @SerializedName("contact")
    private ArrayList<HashMap<String,String>> contact;

    public ArrayList<HashMap<String,String>> getContact(){
        return contact;
    }

}

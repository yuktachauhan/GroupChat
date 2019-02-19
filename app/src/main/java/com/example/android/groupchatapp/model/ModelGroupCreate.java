package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

public class ModelGroupCreate {

    @SerializedName("name")
    private String name;

    @SerializedName("group_id")
    private String id;

    public ModelGroupCreate(String name){
        this.name = name;
    }

    public String getName(){return name;}

    public String getId(){return id;}

}

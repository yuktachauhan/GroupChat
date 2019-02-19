package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ModelGroupList {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("admin")
    private int admin;

    @SerializedName("members")
    public List<Integer> members = new ArrayList<Integer>();

    public void setId(int id){
        this.id=id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public void setMembers(List<Integer> members) {
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getAdmin() {
        return admin;
    }

    public List<Integer> getMembers() {
        return members;
    }
}
package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ModelMessage {

    @SerializedName("id")
    public int id;

    @SerializedName("message")
    private String message;

    /*@SerializedName("messaged_by")
    private HashMap<String,String> messaged_by;*/

    @SerializedName("messaged_by")
    private int messaged_by;

    @SerializedName("file_message")
    private String file_message;

    @SerializedName("time")
    private String time;

    @SerializedName("group")
    private int group;

    public ModelMessage(){}

    public ModelMessage(String msg){
        this.message=msg;
    }

    public void setMessage(String msg){
        this.message=msg;
    }

   /* public void setMessaged_by(HashMap<String,String> msg_by){
        this.messaged_by=msg_by;
    };*/
   public void setMessaged_by(int msg_by){
       this.messaged_by=msg_by;
   };
    public void setFile_message(String file){
        this.file_message=file;
    }

    public void setTime(String time){
        this.time=time;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setGroup(int group){
        this.group=group;
    }

    public int getId(){
        return id;
    }

    public int getGroup(){
        return group;
    }

    public String getMessage(){
        return message;
    }

    public String getTime(){
        return time;
    }

    public String getFile_message(){
        return file_message;
    }

    public int getMessaged_by(){
        return messaged_by;
    }


    /*public HashMap<String,String> getMessaged_by(){
        return messaged_by;
    }*/
}

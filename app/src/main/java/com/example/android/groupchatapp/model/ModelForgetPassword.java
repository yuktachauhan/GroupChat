package com.example.android.groupchatapp.model;

import com.google.gson.annotations.SerializedName;

public class ModelForgetPassword {
      @SerializedName("email")
      private String email;

      @SerializedName("otp")
      private int otp;

      @SerializedName("new_password")
      private String new_password;

      @SerializedName("confirm_password")
      private String confirm_password;

      @SerializedName("user_id")
      private int user_id;

      @SerializedName("info")
      private String info;


      public ModelForgetPassword(String email){
          this.email=email;
      }

      public ModelForgetPassword(int otp,String new_password,String confirm_password){
          this.otp=otp;
          this.new_password=new_password;
          this.confirm_password=confirm_password;
      }

      public String getEmail(){
          return email;
      }

      public String getNew_password(){
          return new_password;
      }

      public String getConfirm_password(){
          return confirm_password;
      }

      public int getOtp(){
          return otp;
      }

      public int getUser_id(){
          return user_id;
      }

      public String getInfo(){
          return info;
      }
}

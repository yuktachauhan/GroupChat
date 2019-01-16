package com.example.android.groupchatapp.rest;

import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.model.ModelSignUp;
import com.example.android.groupchatapp.model.ModelToken;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(" ")
    Call<ModelSignUp>  Register(@Body ModelSignUp modelSignUp);

    @POST("login/")
    Call<ModelLogin> Login(@Body ModelLogin modelLogin);

    @POST("obtain/")
    Call<ModelToken> Token(@Body ModelToken modelToken);

    @POST("createprofile/")
    Call<ModelProfile> profile(@Body ModelProfile modelProfile);
}

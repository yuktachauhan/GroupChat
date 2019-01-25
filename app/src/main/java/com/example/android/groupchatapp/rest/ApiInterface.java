package com.example.android.groupchatapp.rest;

import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.model.ModelSignUp;
import com.example.android.groupchatapp.model.ModelToken;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface ApiInterface {


    @POST("api/register/")
    Call<ModelSignUp>  Register(@Body ModelSignUp modelSignUp);

    @POST("login/")
    Call<ModelLogin> Login(@Body ModelLogin modelLogin);

    @POST("obtain/")
    Call<ModelToken> Token(@Body ModelToken modelToken);

    @POST("verify/")
    Call<ResponseBody> Authentication(@Header("Authorization") String token);

    @PUT("profile/{id}")
    Call<ModelProfile> profile(@Path("id") int id, @Body ModelProfile modelProfile, @Header("Authorization") String authHeader);



}

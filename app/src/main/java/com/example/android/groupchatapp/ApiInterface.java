package com.example.android.groupchatapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST(" ")
    Call<ModelSignUp>  Register(@Body ModelSignUp modelSignUp);

    @POST("api-token-auth/")
    Call<ModelLogin> Login(@Body ModelLogin modelLogin);
}

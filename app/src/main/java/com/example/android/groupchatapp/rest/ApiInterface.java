package com.example.android.groupchatapp.rest;

import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.model.ModelSignUp;
import com.example.android.groupchatapp.model.ModelToken;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface ApiInterface {


    @POST(" ")
    Call<ModelSignUp>  Register(@Body ModelSignUp modelSignUp);

    @POST("login/")
    Call<ModelLogin> Login(@Body ModelLogin modelLogin);

    @POST("obtain/")
    Call<ModelToken> Token(@Body ModelToken modelToken);

    @POST("verify/")
    Call<ResponseBody> Authentication(@Header("Authorization") String token);

    @Multipart
    @POST("creategroup/")
    Call<ResponseBody> createGroup(@Part MultipartBody.Part file,@Part("name") RequestBody name, @Header("Authorization") String authHeader);


    @Multipart
    @PUT("profile/{id}/")
    Call<ResponseBody> profile(@Path("id") int id, @Part MultipartBody.Part file , @Part("name") RequestBody name,@Part("phone_number")
                              String phone, @Header("Authorization") String authHeader);

    @GET("profile/{id}/")
    Call<ModelProfile> getProfile(@Path("id") int id,@Header("Authorization") String authHeader);

    @GET("logout/")
    Call<ResponseBody> logOut(@Header("Authorization") String authHeader);

    @POST("contactlist/")
    Call<ResponseBody> createContactList();

}

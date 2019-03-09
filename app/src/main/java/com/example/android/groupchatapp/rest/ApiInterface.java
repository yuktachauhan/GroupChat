package com.example.android.groupchatapp.rest;

import com.example.android.groupchatapp.model.ModelGroupCreate;
import com.example.android.groupchatapp.model.ModelGroupList;
import com.example.android.groupchatapp.model.ModelMemberAdd;
import com.example.android.groupchatapp.model.ModelMessage;
import com.example.android.groupchatapp.model.NumberListModel;
import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.model.ModelSignUp;
import com.example.android.groupchatapp.model.ModelToken;

import java.util.ArrayList;
import java.util.HashMap;

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
    @PUT("profile/{id}/")
    Call<ResponseBody> profile(@Path("id") int id, @Part MultipartBody.Part file , @Part("name") RequestBody name,@Part("phone_number")
                              String phone, @Header("Authorization") String authHeader);

    @PUT("profile/{id}/")
    Call<ModelProfile> profileNoImg(@Path("id") int id,@Body ModelProfile modelProfile, @Header("Authorization") String authHeader);

    @GET("profile/{id}/")
    Call<ModelProfile> getProfile(@Path("id") int id,@Header("Authorization") String authHeader);

    @GET("logout/")
    Call<ResponseBody> logOut(@Header("Authorization") String authHeader);

    @POST("contactlist/")
    Call<NumberListModel> createContactList(@Body HashMap modelNumberLists, @Header("Authorization") String authHeader);

    @GET("api/groupprofile/{id}/")
    Call<ModelProfile> groupProfile(@Path("id") int id,@Header("Authorization") String authHeader);

    @Multipart
    @PUT("api/groupprofile/{id}/")
    Call<ModelProfile> updateGroupProfile(@Path("id") int id,@Part MultipartBody.Part file,
                                          @Part("name") RequestBody name, @Header("Authorization") String authHeader);

    @Multipart
    @POST("new_group/")
    Call<ModelGroupCreate> createGroup(@Part MultipartBody.Part file ,@Part("name") RequestBody name,@Header("Authorization") String authHeader);

    @POST("new_group/")
    Call<ModelGroupCreate> createGroup(@Body ModelGroupCreate modelGroupCreate,@Header("Authorization") String authHeader);

    @GET("group/")
    Call<ArrayList<ModelGroupList>> groupList(@Header("Authorization") String authHeader);

    @PUT("api/groupprofile/{id}/add_member/")
    Call<ResponseBody> add_member(@Path("id") int id, @Body ModelMemberAdd modelMemberAdd, @Header("Authorization") String authHeader);

    //Message Post
    @POST("group/{id}/")
    Call<ModelMessage> message(@Path("id") int id,@Body ModelMessage modelMessage,@Header("Authorization") String authHeader);

    //Message get
    @GET("group/{id}/")
    Call<ArrayList<ModelMessage>> getmessage(@Path("id") int id,@Header("Authorization") String authHeader);

    //delete member
    @PUT("api/groupprofile/{id}/delete_member/")
    Call<ResponseBody> deleteMember(@Path("id") int id, @Body ModelMemberAdd modelMemberAdd, @Header("Authorization") String authHeader);
}

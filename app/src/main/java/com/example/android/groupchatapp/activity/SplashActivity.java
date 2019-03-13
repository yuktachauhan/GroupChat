package com.example.android.groupchatapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.groupchatapp.Controller;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelToken;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String username;
    String password;
    Controller mController;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences("login", 0);
        username = sharedPreferences.getString("username", "");
        password = sharedPreferences.getString("password", "");
        mController = (Controller) getApplicationContext();


        if (!username.isEmpty() && !password.isEmpty()) {
            login();

        } else {
            new Timer().schedule(new TimerTask() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
            }, 2000);
        }
    }

    public void login(){
        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);

        ModelLogin modelLogin = new ModelLogin(username, password);

        mController.setModelLogin(modelLogin);

        Call<ModelLogin> call=apiInterface.Login(mController.getModelLogin());

        call.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                if (response.isSuccessful()) {
                    myToken();

                }else{
                    Toast.makeText(SplashActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                Toast.makeText(SplashActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void myToken(){
        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);

        ModelToken modelToken = new ModelToken(username,password);

        mController.setModelToken(modelToken);

        Call<ModelToken> call = apiInterface.Token(mController.getModelToken());

        call.enqueue(new Callback<ModelToken>() {
            @Override
            public void onResponse(Call<ModelToken> call, Response<ModelToken> response) {

                if(response.isSuccessful()) {
                    token=response.body().getToken();
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("token",token);
                    editor.apply();
                    Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(SplashActivity.this,"Response is not Successful",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ModelToken> call, Throwable t) {

                Toast.makeText(SplashActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}


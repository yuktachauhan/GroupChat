package com.example.android.groupchatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
private EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.email);
    }

    public void Login(View view){
        String user = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        ApiInterface apiInterface= ApiClient.ApiClient().create(ApiInterface.class);
        ModelLogin modelLogin =new ModelLogin(user,pwd);
        Call<ModelLogin> call=apiInterface.Login(modelLogin);
        call.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                Toast.makeText(LoginActivity.this,"Logged in",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

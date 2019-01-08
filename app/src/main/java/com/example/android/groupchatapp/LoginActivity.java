package com.example.android.groupchatapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
private EditText username,password;
private String token;

String user,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);

    }
    public void doLogin(View view){
        Login();
    }


    public void Login(){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

         user = username.getText().toString().trim();
         pwd = password.getText().toString().trim();

        ApiInterface apiInterface= ApiClient.ApiClient().create(ApiInterface.class);

        ModelLogin modelLogin = new ModelLogin(user,pwd);

        Call<ModelLogin> call=apiInterface.Login(modelLogin);

        call.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                progressDialog.dismiss();
                Log.i("LoginActivity","ON RESPONSE IS CALLED");
                Toast.makeText(LoginActivity.this,"Login is called",Toast.LENGTH_SHORT).show();
                myToken();

            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("LoginActivity","ON FAILURE IS CALLED");
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void myToken() {

        Log.i("LOGIN_ACTIVITY", user);
        Log.i("LOGIN_ACTIVITY", pwd);
        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);

        ModelToken modelToken = new ModelToken(user, pwd);

        Call<ModelToken> call = apiInterface.Token(modelToken);

        call.enqueue(new Callback<ModelToken>() {
            @Override
            public void onResponse(Call<ModelToken> call, Response<ModelToken> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();
                    SharedPreferences myPref = getApplicationContext().getSharedPreferences("my_pref", 0);  //0 means private mode
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("token", response.body().getToken()).commit();
                    token = myPref.getString("token", response.body().getToken());
                    Log.i("MY STORED TOKEN IS =", token);
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<ModelToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

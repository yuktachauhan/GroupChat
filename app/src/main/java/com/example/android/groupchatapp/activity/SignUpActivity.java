package com.example.android.groupchatapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groupchatapp.Controller;
import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelToken;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;
import com.example.android.groupchatapp.model.ModelSignUp;
import com.example.android.groupchatapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    private EditText email,username,password,confirm_password;
    Controller mController;
    private ApiInterface apiInterface;
    private String emailId,user,pwd,confirm;
    ProgressDialog progressDialog;
    /*private static int  user_id;
    private static String token;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email=(EditText) findViewById(R.id.email);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        confirm_password=(EditText) findViewById(R.id.confirm_password);
        mController = (Controller) getApplicationContext();
        apiInterface =ApiClient.ApiClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(SignUpActivity.this);
    }

    public void SignUp(View view){

        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        emailId = email.getText().toString().trim();
        user = username.getText().toString().trim();
        pwd = password.getText().toString().trim();
        confirm = confirm_password.getText().toString().trim();

        if(user.isEmpty()){
            progressDialog.dismiss();
            username.setError("username should not be empty");
            username.requestFocus();
            return;
        }

        if(emailId.isEmpty()){
            progressDialog.dismiss();
            email.setError("email should not be empty");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
                progressDialog.dismiss();
                email.setError("Enter a valid email");
                email.requestFocus();
                return;
        }
        if(pwd.isEmpty()){
            progressDialog.dismiss();
            password.setError("password should not be empty");
            password.requestFocus();
            return;
        }

        ModelSignUp modelSignUp=new ModelSignUp(emailId,user,pwd,confirm);
        mController.setModelSignUp(modelSignUp);

        Call<ModelSignUp> call=apiInterface.Register(modelSignUp);

        call.enqueue(new Callback<ModelSignUp>() {
           @Override
           public void onResponse(retrofit2.Call<ModelSignUp> call, Response<ModelSignUp> response) {
               progressDialog.dismiss();
               if (response.isSuccessful()){
                   Toast.makeText(SignUpActivity.this,"Confirm your email and login.",Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                   startActivity(intent);
           }
               else
                   Toast.makeText(SignUpActivity.this,"Something went wrong",Toast.LENGTH_SHORT);
           }

           @Override
           public void onFailure(retrofit2.Call<ModelSignUp> call, Throwable t) {
               progressDialog.dismiss();
               Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

   /* //login and token fetch already at the time of sign up
   public void selfLogin(){
       progressDialog.setMessage("Signing up...");
       progressDialog.show();

       ModelLogin modelLogin = new ModelLogin(user,pwd);

       mController.setModelLogin(modelLogin);

       Call<ModelLogin> call=apiInterface.Login(mController.getModelLogin());

       call.enqueue(new Callback<ModelLogin>() {
           @Override
           public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
               progressDialog.dismiss();
               if (response.isSuccessful()) {
                   *//*Toast.makeText(LoginActivity.this, "user_id" + response.body().getUser_id(), Toast.LENGTH_SHORT).show();*//*

                   SharedPreferences myPref = getApplicationContext().getSharedPreferences("my_pref", 0);  //0 means private mode
                   SharedPreferences.Editor editor = myPref.edit();
                   editor.putInt("user_id", response.body().getUser_id()).commit();
                   user_id = myPref.getInt("user_id", response.body().getUser_id());
                   myToken();

               }else{
                   Toast.makeText(SignUpActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
               }
           }

           @Override
           public void onFailure(Call<ModelLogin> call, Throwable t) {
               progressDialog.dismiss();
               Toast.makeText(SignUpActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
           }
       });

   }

   public void myToken(){
       ModelToken modelToken = new ModelToken(user, pwd);

       mController.setModelToken(modelToken);

       Call<ModelToken> call = apiInterface.Token(mController.getModelToken());

       call.enqueue(new Callback<ModelToken>() {
           @Override
           public void onResponse(Call<ModelToken> call, Response<ModelToken> response) {
               if(response.isSuccessful()) {

                   SharedPreferences myPref = getApplicationContext().getSharedPreferences("my_pref", 0);  //0 means private mode
                   SharedPreferences.Editor editor = myPref.edit();
                   editor.putString("token", response.body().getToken()).commit();

                   token = myPref.getString("token",response.body().getToken());

                   Intent intent = new Intent(SignUpActivity.this,HomeActivity.class);
                   startActivity(intent);

               }else{
                   Toast.makeText(SignUpActivity.this,"Response is not Successful",Toast.LENGTH_SHORT).show();
               }


           }

           @Override
           public void onFailure(Call<ModelToken> call, Throwable t) {
               Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });*/
   //}


    public void login(View v){
        Intent intent =new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}

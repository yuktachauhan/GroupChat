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
    private static int user_id;

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
                   Toast.makeText(SignUpActivity.this,"Confirm your email and login.",Toast.LENGTH_LONG).show();
                   user_id=response.body().getUser_id();
                   Intent intent = new Intent(SignUpActivity.this,FragmentContainerActivity.class);
                   intent.putExtra("frag","fragmentotp");
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

    public static int getUser_id(){
        return user_id;
    }

    public void login(View v){
        Intent intent =new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}

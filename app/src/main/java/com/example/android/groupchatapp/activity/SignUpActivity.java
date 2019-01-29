package com.example.android.groupchatapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.groupchatapp.Controller;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;
import com.example.android.groupchatapp.model.ModelSignUp;
import com.example.android.groupchatapp.R;

import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {

    private EditText email,username,password,confirm_password;
     Controller mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email=(EditText) findViewById(R.id.email);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        confirm_password=(EditText) findViewById(R.id.confirm_password);

         mController = (Controller) getApplicationContext();
    }

    public void SignUp(View view){
        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        String emailId = email.getText().toString().trim();
        String user = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String confirm = confirm_password.getText().toString().trim();

        if(!pwd.contentEquals(confirm)){
            password.setError("passwords should match");
        }

        ApiInterface apiInterface =ApiClient.ApiClient().create(ApiInterface.class);
        ModelSignUp modelSignUp=new ModelSignUp(emailId,user,pwd,confirm);
        mController.setModelSignUp(modelSignUp);

       retrofit2.Call<ModelSignUp> call=apiInterface.Register(mController.getModelSignUp());

       call.enqueue(new Callback<ModelSignUp>() {
           @Override
           public void onResponse(retrofit2.Call<ModelSignUp> call, Response<ModelSignUp> response) {
               progressDialog.dismiss();
               if (response.isSuccessful()){
                   Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                   Log.i("Email", mController.getModelSignUp().getEmail());
                   Log.i("username", mController.getModelSignUp().getUsername());
                   Log.i("password", mController.getModelSignUp().getPassword());
                   Log.i("confirm password", mController.getModelSignUp().getConfirm_password());
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

    public void login(View v){
        Intent intent =new Intent(SignUpActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}

package com.example.android.groupchatapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.groupchatapp.Controller;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewActivity extends AppCompatActivity {

    ImageView profile;
    EditText name, number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        getProfile();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profile = (ImageView) findViewById(R.id.profile);
        name = (EditText) findViewById(R.id.Name);
        number = (EditText) findViewById(R.id.phoneNumber);
    }

    public void getProfile(){
        final ProgressDialog progressDialog = new ProgressDialog(ProfileViewActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ApiInterface apiInterface =ApiClient.ApiClient().create(ApiInterface.class);

        Call<ModelProfile> call =apiInterface.getProfile(LoginActivity.getUser_id(),"JWT " + LoginActivity.getToken());

        call.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(Call<ModelProfile> call, Response<ModelProfile> response) {
                progressDialog.dismiss();

                if(response.isSuccessful()) {

                    String imageUrl = response.body().getAvatar();

                    Picasso.with(ProfileViewActivity.this).load(imageUrl).placeholder(R.drawable.group).into(profile);

                    name.setText(response.body().getName());


                    number.setText(response.body().getPhone_number());

                }else{
                    Toast.makeText(ProfileViewActivity.this, "some error occurred", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ModelProfile> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(ProfileViewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void editProfile(View view){
        Intent intent=new Intent(ProfileViewActivity.this,ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ProfileViewActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}

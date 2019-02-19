package com.example.android.groupchatapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelGroupCreate;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupCreateActivity extends AppCompatActivity {

    EditText groupName;
    ImageView groupIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        groupName = (EditText) findViewById(R.id.group_name);
        groupIcon = (ImageView) findViewById(R.id.group_profile);

        //checking the permission of mobile storage access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(GroupCreateActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" +getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        }

        public void createGroup(View view){

        String Name =groupName.getText().toString().trim();

            ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);
            ModelGroupCreate modelGroupCreate = new ModelGroupCreate(Name);
            Call<ModelGroupCreate> call = apiInterface.createGroup(modelGroupCreate,"JWT " + LoginActivity.getToken());

            call.enqueue(new Callback<ModelGroupCreate>() {
                @Override
                public void onResponse(Call<ModelGroupCreate> call, Response<ModelGroupCreate> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(GroupCreateActivity.this,"Successful",Toast.LENGTH_LONG).show();
                        /*Intent intent = new Intent(GroupCreateActivity.this,ContactsActivity.class);
                        startActivity(intent);*/
                    }
                }

                @Override
                public void onFailure(Call<ModelGroupCreate> call, Throwable t) {
                    Toast.makeText(GroupCreateActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }


}

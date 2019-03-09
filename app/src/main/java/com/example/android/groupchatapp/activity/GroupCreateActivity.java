package com.example.android.groupchatapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelGroupCreate;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupCreateActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    EditText groupName;
    CircleImageView groupIcon;
    private static final int REQUEST_WRITE_PERMISSION =0;
    private final static int GALLERY_REQUEST_CODE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Group");
        groupName = (EditText) findViewById(R.id.group_name);
        groupIcon = (CircleImageView) findViewById(R.id.group_profile);
        }

    public void chooseProfile(View view){
        accessStorage();
    }

    public void accessStorage() {
        if (ContextCompat.checkSelfPermission(GroupCreateActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // request the permission
            ActivityCompat.requestPermissions(GroupCreateActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_PERMISSION);

        } else {
            // Permission has already been granted
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    selectImage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(GroupCreateActivity.this,"App will not be able to access the gallery",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    //to select image from gallery
    public void selectImage() {
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        if (pickImage.resolveActivity(getPackageManager()) != null) {
            //we check that at least one app is there to perform our action,if no app was there
            startActivityForResult(pickImage, GALLERY_REQUEST_CODE);
            //then because we check our app will not crash
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();
            }
        }
    }

    public void createGroup(View view){
        final ProgressDialog progressDialog = new ProgressDialog(GroupCreateActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String Name =groupName.getText().toString().trim();
        RequestBody my_name=RequestBody.create(MediaType.parse("text/plain"),Name);

        if(imageUri!=null) {
            File file = new File(getRealPathFromURI(imageUri));
            RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(), mFile);
        }
        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);
        ModelGroupCreate modelGroupCreate = new ModelGroupCreate(Name);
        //Call<ModelGroupCreate> call = apiInterface.createGroup(fileToUpload,my_name,"JWT " + LoginActivity.getToken());
        Call<ModelGroupCreate> call = apiInterface.createGroup(modelGroupCreate,"JWT " + LoginActivity.getToken());
        call.enqueue(new Callback<ModelGroupCreate>() {
            @Override
                public void onResponse(Call<ModelGroupCreate> call, Response<ModelGroupCreate> response) {
                progressDialog.dismiss();
                    if(response.isSuccessful()){
                        Toast.makeText(GroupCreateActivity.this,"Successful",Toast.LENGTH_LONG).show();
                        /*Intent intent = new Intent(GroupCreateActivity.this,ContactsActivity.class);
                        startActivity(intent);*/
                    }
                }

                @Override
                public void onFailure(Call<ModelGroupCreate> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(GroupCreateActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }

//method to access the file or image path from uri
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(GroupCreateActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent =new Intent(getApplicationContext(),HomeActivity.class);
        startActivityForResult(intent,0);
        return true;
    }
}

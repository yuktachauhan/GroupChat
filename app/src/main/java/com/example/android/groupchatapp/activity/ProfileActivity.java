package com.example.android.groupchatapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    ImageView profile;
    EditText name, number;
    String username,phone_number;
    final static int GALLERY_REQUEST_CODE = 1;
    final static int CAMERA_REQUEST_CODE = 0;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile = (ImageView) findViewById(R.id.profile);
        name = (EditText) findViewById(R.id.Name);
        number = (EditText) findViewById(R.id.phoneNumber);
    }

    //to select image from gallery

    public void selectImage(View view) {
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        if (pickImage.resolveActivity(getPackageManager()) != null) {//we check that at least one app is there to perform our action,if no app was there
            startActivityForResult(pickImage, GALLERY_REQUEST_CODE);
            //then because we check our app will not crash

        }
    }

    //to take picture with the help of camera and display in image view
    public void capturePhoto(View view) {
        Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureImage.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(captureImage, CAMERA_REQUEST_CODE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                     imageUri = data.getData();
                    //File imageFile =  new File(getRealPathFromURI(imageUri));
                    //createProfile();

                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    //InputStream: The input stream that holds the raw data to be decoded into a bitmap.
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


                    //decode stream: Decode an input stream into a bitmap.
                    //BitmapFactory Creates Bitmap objects from various sources,including files, streams, and byte-arrays.
                    profile.setImageBitmap(selectedImage);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

        }
       else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                profile.setImageBitmap(imageBitmap);
            }
        } else {
            Toast.makeText(ProfileActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }


    public void createProfile(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        String Name = name.getText().toString().trim();
        String phone_number = number.getText().toString().trim();

            ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);

            ModelProfile modelProfile = new ModelProfile(Name,phone_number);

            //Log.i("getRealPathFromURI(Uri)",getRealPathFromURI(imageUri));


            retrofit2.Call<ModelProfile> call = apiInterface.profile(LoginActivity.getUser_id(),modelProfile,"JWT "+LoginActivity.getToken());


            call.enqueue(new Callback<ModelProfile>() {
                @Override
                public void onResponse(retrofit2.Call<ModelProfile> call, Response<ModelProfile> response) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this,"Profile created",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(retrofit2.Call<ModelProfile> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
    }

   /* private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
*/


  /*  public void profileUpload(){
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        username = name.getText().toString().trim();
        phone_number= number.getText().toString().trim();

        ApiInterface apiInterface= ApiClient.ApiClient().create(ApiInterface.class);

        ModelProfile modelProfile = new ModelProfile(username,getRealPathFromURI(imageUri),phone_number);

        Call<ModelProfile> call=apiInterface.profile(modelProfile);

        call.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(Call<ModelProfile> call, Response<ModelProfile> response) {
                progressDialog.dismiss();
                Log.i("LoginActivity","ON RESPONSE IS CALLED");



            }

            @Override
            public void onFailure(Call<ModelProfile> call, Throwable t) {
                progressDialog.dismiss();
                Log.i("LoginActivity","ON FAILURE IS CALLED");
                Toast.makeText(ProfileActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }*/
}

/***
 * A Uniform Resource Identifier (URI) is a compact sequence of characters that identifies an abstract or physical resource.
 * Bitmap. ... A bitmap is a type of memory organization or image file format used to store digital images.
 * The term bitmap comes from the computer programming terminology, meaning just a map of bits, a spatially mapped array of bits.
 * Advantages of bitmap files include the following: Bitmap files may be easily created from existing pixel data stored in an array in memory.
 * Retrieving pixel data stored in a bitmap file may often be accomplished by using a set of coordinates that allows the data to be conceptualized as a grid.
 * The Bitmap (android.graphics.Bitmap) class represents a bitmap image.
 * You create bitmaps via the BitmapFactory (android.graphics.BitmapFactory) class.
 * Using a BitmapFactory, you can create bitmaps in three common ways: from a resource, a file, or an InputStream.
 * Bitmap (or raster) images are stored as a series of tiny dots called pixels.
 * Each pixel is actually a very small square that is assigned a color, and then arranged in a pattern to form the image.
 * When you zoom in on a bitmap image you can see the individual pixels that make up that image.
 **/
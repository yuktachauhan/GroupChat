package com.example.android.groupchatapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.support.v4.content.CursorLoader;

public class ProfileActivity extends AppCompatActivity {
    ImageView profile;
    EditText name, number;
    final static int GALLERY_REQUEST_CODE = 1;
    Uri imageUri;
    //Bitmap selectedImage;
    //InputStream imageStream;
    Controller mController;
    String Name,phoneNumber;
    ModelProfile modelProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //checking the permission of mobile storage access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            finish();
            startActivity(intent);
            return;
        }

        profile = (ImageView) findViewById(R.id.profile);
        name = (EditText) findViewById(R.id.Name);
        number = (EditText) findViewById(R.id.phoneNumber);
        mController =(Controller) getApplicationContext();
    }

    //to select image from gallery

    public void selectImage(View view) {
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
                createProfile();

                    /*imageStream = getContentResolver().openInputStream(imageUri);
                    //InputStream: The input stream that holds the raw data to be decoded into a bitmap.
                    selectedImage = BitmapFactory.decodeStream(imageStream);


                    //decode stream: Decode an input stream into a bitmap.
                    //BitmapFactory Creates Bitmap objects from various sources,including files, streams, and byte-arrays.




                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }*/

            } /*else {
            Toast.makeText(ProfileActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }*/
        }
    }


    public void createProfile() {
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

         Name = name.getText().toString().trim();
         phoneNumber = number.getText().toString().trim();

        File file = new File(getRealPathFromURI(imageUri));
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody my_name=RequestBody.create(MediaType.parse("text/plain"),Name);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(), mFile);

        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);

         modelProfile = new ModelProfile(Name, phoneNumber);


        retrofit2.Call<ResponseBody> call = apiInterface.profile(LoginActivity.getUser_id(),
                fileToUpload, my_name,modelProfile.getPhone_number()
                , "JWT " + LoginActivity.getToken());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
               if(response.isSuccessful()) {
                   Toast.makeText(ProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                   Intent intent =new Intent(ProfileActivity.this,ProfileViewActivity.class);
                   startActivity(intent);

               }else{
                   Toast.makeText(ProfileActivity.this,"Something went wrong.",Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

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
package com.example.android.groupchatapp.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.groupchatapp.Controller;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.HomeActivity;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.activity.ProfileActivity;
import com.example.android.groupchatapp.activity.ProfileViewActivity;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    AppCompatActivity activity;
    CircleImageView profile;
    EditText name, number;
    Button profile_button;
    final static int GALLERY_REQUEST_CODE = 1;
    Uri imageUri;
    //Bitmap selectedImage;
    //InputStream imageStream;
    Controller mController;
    String Name,phoneNumber;
    ModelProfile modelProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Profile");

        profile = (CircleImageView) view.findViewById(R.id.profile);
        name = (EditText) view.findViewById(R.id.Name);
        number = (EditText) view.findViewById(R.id.phoneNumber);
        profile_button=(Button) view.findViewById(R.id.profile_button);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mController =(Controller) activity.getApplicationContext();
        return view;

    }


    //to select image from gallery

    public void selectImage() {
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        if (pickImage.resolveActivity(activity.getPackageManager()) != null) {
            //we check that at least one app is there to perform our action,if no app was there
            startActivityForResult(pickImage, GALLERY_REQUEST_CODE);
            //then because we check our app will not crash
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == activity.RESULT_OK) {
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
        final ProgressDialog progressDialog = new ProgressDialog(activity);
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
                if(response.isSuccessful()) {
                    Intent intent = new Intent(activity,ProfileViewActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(activity,"Something went wrong.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent =new Intent(activity.getApplicationContext(),HomeActivity.class);
        startActivityForResult(intent,0);
        return true;
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

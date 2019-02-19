package com.example.android.groupchatapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.FragmentContainerActivity;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupUpdateFragment extends Fragment {
    private EditText group_name;
    private ImageView group_image;
    private Button group_update_button,group_profile_choose_button;
    public static final int GALLERY_REQUEST_CODE=1;
    AppCompatActivity activity;
    Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_update_group,container,false);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        group_name =(EditText) view.findViewById(R.id.group_name);
        group_image=(ImageView) view.findViewById(R.id.group_profile);
        group_update_button=(Button) view.findViewById(R.id.group_profile_update_button);
        group_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupUpdate();
            }
        });
        group_profile_choose_button=(Button) view.findViewById(R.id.group_profile_choose_button);
        group_profile_choose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupProfile();
            }
        });
        return view;
    }


    /*@Override
    public void onResume(){
        super.onResume();
        getProfile();
    }*/

    public void groupProfile(){
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
        if(requestCode==GALLERY_REQUEST_CODE){
            if(resultCode==activity.RESULT_OK){
                imageUri=data.getData();
                groupUpdate();
            }
        }
    }

    public void groupUpdate(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String name = group_name.getText().toString().trim();

        File file = new File(getRealPathFromURI(imageUri));
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody my_name=RequestBody.create(MediaType.parse("text/plain"),name);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(), mFile);

        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
        retrofit2.Call<ModelProfile> call=apiInterface.updateGroupProfile(5,fileToUpload,my_name,"JWT " + LoginActivity.getToken());


        call.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(retrofit2.Call<ModelProfile> call, Response<ModelProfile> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    //Toast.makeText(activity,"Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity,FragmentContainerActivity.class);
                    intent.putExtra("frag","group_update");
                    startActivity(intent);

                }else{
                    Toast.makeText(activity,"some error occurred",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ModelProfile> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }
    
    
   /* public void getProfile(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
        Call<ModelProfile> call = apiInterface.groupProfile(5,"JWT " + LoginActivity.getToken());
        call.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(Call<ModelProfile> call, Response<ModelProfile> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String imageUrl = response.body().getAvatar();
                    Picasso.with(activity).load(imageUrl).into(group_image);
                    group_name.setText(response.body().getName());
                }else{
                    Toast.makeText(activity,"some error occurred",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelProfile> call, Throwable t) {
                 progressDialog.dismiss();
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        
    }

*/    private String getRealPathFromURI(Uri contentURI) {
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
}

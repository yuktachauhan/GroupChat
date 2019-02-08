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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.FragmentContainerActivity;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.model.ModelGroupCreate;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupFragment extends Fragment {
    private EditText group_name;
    private ImageView group_image;
    private Button group_create_button,profile_choose_button;
    public static final int GALLERY_REQUEST_CODE=1;
    AppCompatActivity activity;
    Uri imageUri;
    File file;
    TextView text_group_profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    View view= inflater.inflate(R.layout.fragment_create_group,container,false);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        //checking the permission of mobile storage access
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" +activity.getPackageName()));
            activity.finish();
            startActivity(intent);
            return view;
        }

        group_name = (EditText) view.findViewById(R.id.group_name);
    group_image = (ImageView) view.findViewById(R.id.group_profile);
    profile_choose_button=(Button) view.findViewById(R.id.group_profile_choose_button);
    group_create_button=(Button) view.findViewById(R.id.group_profile_create_button);
    text_group_profile=(TextView) view.findViewById(R.id.text_profile_update);

    profile_choose_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            chooseGroupProfile();
        }
    });

    group_create_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            createMyGroup();
        }
    });

    text_group_profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {         //to move from one fragment to another
           Intent intent = new Intent(activity,FragmentContainerActivity.class);
           intent.putExtra("frag","fragment");
           startActivity(intent);
        }
    });

    return view;
}



    public void chooseGroupProfile(){
        Intent pickImage=new Intent(Intent.ACTION_PICK);
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
                    //createMyGroup();
            }
        }
    }

    public void createMyGroup(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        String name = group_name.getText().toString().trim();
        if(imageUri!=null){
         file = new File(getRealPathFromURI(imageUri));
        }
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody my_name=RequestBody.create(MediaType.parse("application/json"),name);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(), mFile);
        ModelGroupCreate modelGroupCreate = new ModelGroupCreate(name);
        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
        retrofit2.Call<ModelGroupCreate> call=apiInterface.createGroup(fileToUpload,my_name,"JWT " + LoginActivity.getToken());


        call.enqueue(new Callback<ModelGroupCreate>() {
            @Override
            public void onResponse(retrofit2.Call<ModelGroupCreate> call, Response<ModelGroupCreate> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity, response.body().getId(), Toast.LENGTH_SHORT).show();


                }else{
                    Toast.makeText(activity,"some error occurred",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ModelGroupCreate> call, Throwable t) {
               progressDialog.dismiss();
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
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

}

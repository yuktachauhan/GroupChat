package com.example.android.groupchatapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.activity.ProfileActivity;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateGroupFragment extends Fragment {
    private EditText group_name;
    private ImageView group_image;
    private Button group_create_button,profile_choose_button;
    public static final int PICK_CONTACT =0;
    public static final int GALLERY_REQUEST_CODE=1;
    AppCompatActivity activity;
    Uri imageUri;
    InputStream imageStream;
    Bitmap selectedImage;

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    View view= inflater.inflate(R.layout.fragment_create_group,container,false);

    android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
    activity = (AppCompatActivity) getActivity();
    activity.setSupportActionBar(toolbar);

    group_name = (EditText) view.findViewById(R.id.group_name);
    group_image = (ImageView) view.findViewById(R.id.group_profile);
    profile_choose_button=(Button) view.findViewById(R.id.group_profile_choose_button);
    group_create_button=(Button) view.findViewById(R.id.group_profile_create_button);

    profile_choose_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            chooseGroupProfile();
        }
    });

    group_create_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            createGroup();
        }
    });

    return view;
}



    public void chooseGroupProfile(){
        Intent pickImage=new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        activity=(AppCompatActivity) getActivity();

        if (pickImage.resolveActivity(activity.getPackageManager()) != null) {
            //we check that at least one app is there to perform our action,if no app was there
            startActivityForResult(pickImage, GALLERY_REQUEST_CODE);
            //then because we check our app will not crash

        }
    }

    public void createGroup(){
          Intent pickContact=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
          startActivityForResult(pickContact, PICK_CONTACT);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == activity.RESULT_OK) {
                try {
                    imageUri = data.getData();
                    //File imageFile =  new File(getRealPathFromURI(imageUri));
                    //createProfile();

                     imageStream = activity.getContentResolver().openInputStream(imageUri);
                    //InputStream: The input stream that holds the raw data to be decoded into a bitmap.
                      selectedImage = BitmapFactory.decodeStream(imageStream);

                     createMyGroup();
                    //decode stream: Decode an input stream into a bitmap.
                    //BitmapFactory Creates Bitmap objects from various sources,including files, streams, and byte-arrays.



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            }

        if(requestCode==PICK_CONTACT) {
            if(resultCode==activity.RESULT_OK){
                {
                    Uri contactData = data.getData();
                    Cursor c = activity.managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst())
                    {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            Cursor phones = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            phones.moveToFirst();
                            String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        }
                    }
                }
            }

            }

    }

    public void createMyGroup(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String name = group_name.getText().toString().trim();

        File file=new File(getRealPathFromURI(imageUri));
        RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody my_name=RequestBody.create(MediaType.parse("text/plain"),name);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("avatar", file.getName(), mFile);

        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
        retrofit2.Call<ResponseBody> call=apiInterface.createGroup(fileToUpload,my_name,LoginActivity.getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Toast.makeText(activity,"group created",Toast.LENGTH_SHORT).show();
                group_image.setImageBitmap(selectedImage);
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
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

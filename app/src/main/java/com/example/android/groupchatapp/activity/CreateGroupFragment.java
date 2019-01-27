package com.example.android.groupchatapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateGroupFragment extends Fragment {
    private EditText group_name;
    private ImageView group_image;
    private Button group_create_button,profile_choose_button;
    public static final int PICK_CONTACT =0;
    public static final int GALLERY_REQUEST_CODE=1;
    AppCompatActivity activity;
    Uri imageUri;

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


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == activity.RESULT_OK) {
                try {
                    imageUri = data.getData();
                    //File imageFile =  new File(getRealPathFromURI(imageUri));
                    //createProfile();

                    final InputStream imageStream = activity.getContentResolver().openInputStream(imageUri);
                    //InputStream: The input stream that holds the raw data to be decoded into a bitmap.
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


                    //decode stream: Decode an input stream into a bitmap.
                    //BitmapFactory Creates Bitmap objects from various sources,including files, streams, and byte-arrays.
                    group_image.setImageBitmap(selectedImage);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            }

        if(requestCode==PICK_CONTACT) {
            if(resultCode==activity.RESULT_OK){

            }

            }

    }


}

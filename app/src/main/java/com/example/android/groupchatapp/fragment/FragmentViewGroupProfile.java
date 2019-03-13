package com.example.android.groupchatapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.FragmentContainerActivity;
import com.example.android.groupchatapp.activity.HomeActivity;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.activity.MessageActivity;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentViewGroupProfile extends Fragment {

    AppCompatActivity activity;
    TextView group_name;
    CircleImageView group_image;
    SharedPreferences sharedPreferences;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_profile, container, false);
        activity = (AppCompatActivity) getActivity();
        sharedPreferences= activity.getSharedPreferences("login",Context.MODE_PRIVATE);
        token=sharedPreferences.getString("token","");
        getProfile();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);

        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("My Profile");
        group_name = (TextView) view.findViewById(R.id.group_name);
        group_image = (CircleImageView) view.findViewById(R.id.group_profile);
        return view;
    }

    public void getProfile(){
        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);

        Call<ModelProfile> call =apiInterface.groupProfile(MessageActivity.getGroup_id(),"JWT " + token);

        call.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(Call<ModelProfile> call, Response<ModelProfile> response) {
                if(response.isSuccessful()){
                    String imageUrl=response.body().getAvatar();
                    Picasso.with(activity).load(imageUrl).placeholder(R.drawable.group).into( group_image);
                    group_name.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<ModelProfile> call, Throwable t) {
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent =new Intent(activity.getApplicationContext(),HomeActivity.class);
        startActivityForResult(intent,0);
        return true;
    }


}
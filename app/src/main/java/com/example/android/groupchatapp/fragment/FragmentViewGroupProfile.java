package com.example.android.groupchatapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.FragmentContainerActivity;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.model.ModelProfile;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentViewGroupProfile extends Fragment {

    AppCompatActivity activity;
    TextView group_name;
    ImageView group_image;
    Button group_update;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_profile, container, false);

        getProfile();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        group_name = (TextView) view.findViewById(R.id.group_name);
        group_image = (ImageView) view.findViewById(R.id.group_profile);
        group_update=  (Button) view.findViewById(R.id.group_profile_update_button);
        group_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,FragmentContainerActivity.class);
                intent.putExtra("frag","view_group_profile");
                startActivity(intent);
            }
        });

        return view;
    }

    public void getProfile(){
        ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);

        Call<ModelProfile> call =apiInterface.groupProfile(5,"JWT " + LoginActivity.getToken());

        call.enqueue(new Callback<ModelProfile>() {
            @Override
            public void onResponse(Call<ModelProfile> call, Response<ModelProfile> response) {
                if(response.isSuccessful()){
                    String imageUrl=response.body().getAvatar();
                    Picasso.with(activity).load(imageUrl).into( group_image);
                    group_name.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<ModelProfile> call, Throwable t) {
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
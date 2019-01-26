package com.example.android.groupchatapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android.groupchatapp.R;

public class CreateGroupFragment extends Fragment {
    private EditText group_name;
    private ImageView group_image;
    private Button group_create_button,profile_choose_button;

@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    View view= inflater.inflate(R.layout.fragment_create_group,container,false);

    android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
    AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.setSupportActionBar(toolbar);

    group_name = (EditText) view.findViewById(R.id.group_name);
    group_image = (ImageView) view.findViewById(R.id.group_profile);
    profile_choose_button=(Button) view.findViewById(R.id.group_profile_choose_button);
    group_create_button=(Button) view.findViewById(R.id.group_profile_create_button);

    return view;
}
}

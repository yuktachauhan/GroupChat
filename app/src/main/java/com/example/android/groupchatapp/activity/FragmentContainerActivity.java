package com.example.android.groupchatapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.fragment.FragmentViewGroupProfile;
import com.example.android.groupchatapp.fragment.GroupUpdateFragment;
import com.example.android.groupchatapp.fragment.MemberListFragment;
import com.example.android.groupchatapp.fragment.ResponseContactListFragment;

public class FragmentContainerActivity extends AppCompatActivity {

    String frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }
     @Override
    public void onResume(){
        super.onResume();
        Intent intent =getIntent();
        if(intent.getExtras()!=null){
             frag = intent.getStringExtra("frag");
        }
        switch (frag){
            case "fragment":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,new FragmentViewGroupProfile()).commit();
                break;

            case "group_update":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,new GroupUpdateFragment()).commit();
                break;

            case "view_group_profile":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,new FragmentViewGroupProfile()).commit();
                break;
            case "responseContactList":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,new ResponseContactListFragment()).commit();
                break;

            case "memberList":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2,new MemberListFragment()).commit();
                break;
        }
    }
}

package com.example.android.groupchatapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groupchatapp.MemberListAdapter;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.HomeActivity;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.activity.MessageActivity;
import com.example.android.groupchatapp.model.ModelMemberAdd;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberListFragment extends Fragment {

    AppCompatActivity activity;
    private RecyclerView recyclerView;
    private MemberListAdapter memberListAdapter;
    private HashMap<String,String> hashMap;
    public static final String TAG ="MemberList";
    String name,phone;
    CircleImageView delete_button;
    SharedPreferences sharedPreferences;
    private String token;
    private int userId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.response_contactlist_fragment, container, false);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        activity = (AppCompatActivity) getActivity();

        sharedPreferences= activity.getSharedPreferences("login",0);
        token=sharedPreferences.getString("token","");
        userId=sharedPreferences.getInt("user_id",0);


        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Member List");
        delete_button=(CircleImageView) view.findViewById(R.id.image_button);
        delete_button.setImageResource(R.drawable.delete);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"delete member",Toast.LENGTH_LONG).show();
                deleteMember();
            }
        });
        recyclerView =(RecyclerView) view.findViewById(R.id.recycler_response_contacts);
        memberListAdapter= new MemberListAdapter(activity,MessageActivity.getMember_list());
        memberListAdapter.setOnItemClickListener(new MemberListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(activity,position+"",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(int position, View v) {

                name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).
                        itemView.findViewById(R.id.default_text_member)).getText().toString();
                phone =((TextView) recyclerView.findViewHolderForAdapterPosition(position).
                        itemView.findViewById(R.id.default_text_number)).getText().toString();

                hashMap=new HashMap<String, String>();
                hashMap.put(name,phone);
                Toast.makeText(activity,hashMap+"",Toast.LENGTH_LONG).show();

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(memberListAdapter);
        memberListAdapter.notifyDataSetChanged();
        return view;
    }

    public void deleteMember(){
        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);
        ModelMemberAdd modelMemberAdd = new ModelMemberAdd(hashMap);
        Call<ResponseBody> call= apiInterface.deleteMember(MessageActivity.getGroup_id(),modelMemberAdd,"JWT "
                + token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(activity,"Member deleted",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(activity,"some error occurred",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(activity,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent =new Intent(activity.getApplicationContext(),MessageActivity.class);
        startActivityForResult(intent,0);
        return true;
    }
}

package com.example.android.groupchatapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groupchatapp.ContactListAdapter;
import com.example.android.groupchatapp.GrouplistAdapter;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.ContactsActivity;
import com.example.android.groupchatapp.activity.HomeActivity;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.model.ModelMemberAdd;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseContactListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactListAdapter contactListAdapter;
    AppCompatActivity activity;
    private HashMap<String,String> hashMap;
    public static final String TAG ="ResponseContactList";
    String name,phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.response_contactlist_fragment, container, false);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        recyclerView =(RecyclerView) view.findViewById(R.id.recycler_response_contacts);
        contactListAdapter = new ContactListAdapter(ContactsActivity.getArrayList());
        contactListAdapter.setOnItemClickListener(new ContactListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //on clicking the item we want the member to be added in the group,member information can be
                //taken from the textviews in recyclerview and we can send name and number of member in the form of hashmap
                 name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).
                        itemView.findViewById(R.id.default_text_member)).getText().toString();
                 phone =((TextView) recyclerView.findViewHolderForAdapterPosition(position).
                        itemView.findViewById(R.id.default_text_number)).getText().toString();

                 hashMap=new HashMap<String, String>();
                 hashMap.put(name,phone);
                 addMember();

            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(contactListAdapter);

        prepareContactsList();

        return view;

    }

    public void prepareContactsList(){
        contactListAdapter.notifyDataSetChanged();
    }

    public void addMember(){
        /*hashMap=new HashMap<String, String>();
        hashMap.put(name,phone);*/
        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);
        ModelMemberAdd modelMemberAdd = new ModelMemberAdd(hashMap);
        Call<ResponseBody> call= apiInterface.add_member(1,modelMemberAdd,"JWT " + LoginActivity.getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                   Toast.makeText(activity,"MemberAdded",Toast.LENGTH_LONG).show();
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
}

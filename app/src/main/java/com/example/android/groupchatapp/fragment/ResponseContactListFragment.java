package com.example.android.groupchatapp.fragment;

import android.content.Intent;
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

import com.example.android.groupchatapp.ContactListAdapter;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.activity.LoginActivity;
import com.example.android.groupchatapp.activity.MessageActivity;
import com.example.android.groupchatapp.model.ModelAddMemberListResponse;
import com.example.android.groupchatapp.model.ModelMemberAdd;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
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
    CircleImageView add_button;
    ArrayList<HashMap<String,String>> memberList=new ArrayList<>();
    private HashMap<String,String> memberHashmap=new HashMap<>();
    ArrayList<HashMap<String,String>> responseMemberlist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.response_contactlist_fragment, container, false);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle("Add Members");
        add_button=(CircleImageView) view.findViewById(R.id.image_button);
        add_button.setImageResource(R.drawable.addmember);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember();
            }
        });
        recyclerView =(RecyclerView) view.findViewById(R.id.recycler_response_contacts);
        putMemberList();
        return view;

    }

    public void putMemberList(){
        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);
        memberList=LoginActivity.getArrayList();
        for(int i=0;i<memberList.size();i++){
            HashMap<String,String> hashMap=memberList.get(i);
            for(String key:hashMap.keySet()){
                String value=hashMap.get(key);
                memberHashmap.put(key,value);
            }
        }
        ModelAddMemberListResponse modelAddMemberListResponse=new ModelAddMemberListResponse(memberHashmap);
        Call<ModelAddMemberListResponse> call =apiInterface.memberList(MessageActivity.getGroup_id(),modelAddMemberListResponse
                ,"JWT "+LoginActivity.getToken());
        call.enqueue(new Callback<ModelAddMemberListResponse>() {
            @Override
            public void onResponse(Call<ModelAddMemberListResponse> call, Response<ModelAddMemberListResponse> response) {
                if (response.isSuccessful()) {
                    responseMemberlist = response.body().getMember_list();
                    Toast.makeText(activity,responseMemberlist+"",Toast.LENGTH_SHORT).show();
                    contactListAdapter = new ContactListAdapter(responseMemberlist);
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

                        }

                        @Override
                        public void onItemLongClick(int position, View v) {
                            name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).
                                    itemView.findViewById(R.id.default_text_member)).getText().toString();
                            phone =((TextView) recyclerView.findViewHolderForAdapterPosition(position).
                                    itemView.findViewById(R.id.default_text_number)).getText().toString();

                            hashMap=new HashMap<String, String>();
                            hashMap.put(name,phone);
                        }
                    });
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(contactListAdapter);

                    prepareContactsList();

                }else{
                    Toast.makeText(activity,"some error occurred", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onFailure(Call<ModelAddMemberListResponse> call, Throwable t) {
                Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void prepareContactsList(){
        contactListAdapter.notifyDataSetChanged();
    }

    public void addMember(){
        /*hashMap=new HashMap<String, String>();
        hashMap.put(name,phone);*/
        ApiInterface apiInterface = ApiClient.ApiClient().create(ApiInterface.class);
        ModelMemberAdd modelMemberAdd = new ModelMemberAdd(hashMap);
        Call<ResponseBody> call= apiInterface.add_member(MessageActivity.getGroup_id(),
                modelMemberAdd,"JWT " + LoginActivity.getToken());

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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent =new Intent(activity.getApplicationContext(),MessageActivity.class);
        startActivityForResult(intent,0);
        return true;
    }

}

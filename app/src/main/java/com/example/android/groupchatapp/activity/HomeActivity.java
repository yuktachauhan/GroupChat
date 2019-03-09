package com.example.android.groupchatapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groupchatapp.ContactListAdapter;
import com.example.android.groupchatapp.GrouplistAdapter;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelGroupList;
import com.example.android.groupchatapp.model.NumberListModel;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ArrayList<ModelGroupList> groupLists; //group information
    private RecyclerView recyclerView;
    private GrouplistAdapter grouplistAdapter;
    public static final String TAG ="HomeActivity";
    public static int group_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        groupListShow();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chatty!");
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        /*
         *Enable the app bar's "home" button by calling setDisplayHomeAsUpEnabled(true),
         * and then change it to use the menu icon by calling setHomeAsUpIndicator(int), as shown here:
         * */
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                if(menuItem.getItemId()==R.id.profile_drawer){

                    Intent intent = new Intent(HomeActivity.this,ProfileViewActivity.class);
                    startActivity(intent);
                }

                if(menuItem.getItemId()==R.id.create_group){
                    Intent intent = new Intent(HomeActivity.this,GroupCreateActivity.class);
                    startActivity(intent);
                }

                if(menuItem.getItemId()==R.id.logout){

                    final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                    progressDialog.setMessage("Logging Out");
                    progressDialog.show();

                    ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
                    Call<ResponseBody> call =apiInterface.logOut("JWT " + LoginActivity.getToken());

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            if (response.isSuccessful()) {
                                Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(HomeActivity.this, "not logout", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                return true;
            }
        });

    }




    //to show groups in recyclerview
    public void groupListShow(){

        ApiInterface apiInterface= ApiClient.ApiClient().create(ApiInterface.class);

        Call<ArrayList<ModelGroupList>> call =apiInterface.groupList("JWT " + LoginActivity.getToken());

        call.enqueue(new Callback<ArrayList<ModelGroupList>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelGroupList>> call, Response<ArrayList<ModelGroupList>> response) {

                if(response.isSuccessful()) {
                    groupLists=response.body();
                    recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
                    grouplistAdapter = new GrouplistAdapter(HomeActivity.this,groupLists);
                    grouplistAdapter.setOnItemClickListener(new GrouplistAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            /*String groupId = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).
                                    itemView.findViewById(R.id.default_group_id)).getText().toString().trim();
                            group_id = Integer.parseInt(groupId);
                            Log.i("GroupId",group_id+"");*/
                            Intent intent =new Intent(HomeActivity.this,MessageActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(int position, View v) {

                        }
                    });
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.addItemDecoration(new DividerItemDecoration(HomeActivity.this, LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(grouplistAdapter);
                    prepareGroupList();

                }else{
                    Toast.makeText(HomeActivity.this, "not successful", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ModelGroupList>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void prepareGroupList(){
        grouplistAdapter.notifyDataSetChanged();
    }


    //To open the navigation drawer when we click on the nav drawer button (three parallel lines)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void groupCreate(View view) {
        Intent intent = new Intent(HomeActivity.this,GroupCreateActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

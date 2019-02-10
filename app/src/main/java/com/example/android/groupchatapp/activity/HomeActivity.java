package com.example.android.groupchatapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelGroupList;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        /*
         *Enable the app bar's "home" button by calling setDisplayHomeAsUpEnabled(true),
         * and then change it to use the menu icon by calling setHomeAsUpIndicator(int), as shown here:
         * */
        icon =(ImageView) findViewById(R.id.group_icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               groupListShow();
            }
        });
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

    public void groupListShow(){
        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Sending...");
        progressDialog.show();

        ApiInterface apiInterface= ApiClient.ApiClient().create(ApiInterface.class);

        Call<ModelGroupList> call =apiInterface.groupList("JWT " + LoginActivity.getToken());

       call.enqueue(new Callback<ModelGroupList>() {
           @Override
           public void onResponse(Call<ModelGroupList> call, Response<ModelGroupList> response) {
               progressDialog.dismiss();
               if(response.isSuccessful()) {
                   Toast.makeText(HomeActivity.this, response.body().getName(), Toast.LENGTH_LONG).show();
               }else{
                   Toast.makeText(HomeActivity.this, "not sucessfull", Toast.LENGTH_LONG).show();
               }
           }

           @Override
           public void onFailure(Call<ModelGroupList> call, Throwable t) {
               progressDialog.dismiss();
               Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
           }
       });


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


   /* public void groupCreate(View view) {
        Intent intent = new Intent(HomeActivity.this,GroupCreateActivity.class);
        startActivity(intent);
    }*/
}

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
    ImageView icon;
    ArrayList<ModelGroupList> groupLists; //group information
    public static final int REQUEST_READ_CONTACTS=1;
    HashMap<String,String> hashMap;//for sending all contacts
    private static ArrayList<HashMap<String,String>> arrayList;  //for contacts
    private RecyclerView recyclerView;
    private GrouplistAdapter grouplistAdapter;
    public static final String TAG ="HomeActivity";
    public static int group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        accessContacts();  //permission method to access contacts
        //groupListShow();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    public void accessContacts() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // request the permission
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);

            // REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.

        } else {
            // Permission has already been granted
            createContacts();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createContacts();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(HomeActivity.this,"You cannot chat if you deny the permission.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void createContacts(){

        ApiInterface apiInterface =ApiClient.ApiClient().create(ApiInterface.class);

        NumberListModel numberListModel = new NumberListModel();

        Call<NumberListModel> call=apiInterface.createContactList(getAllContacts(),"JWT " + LoginActivity.getToken());
        call.enqueue(new Callback<NumberListModel>() {
            @Override
            public void onResponse(Call<NumberListModel> call, Response<NumberListModel> response) {

                if(response.isSuccessful()){
                    arrayList=new ArrayList<HashMap<String, String>>();
                    arrayList=response.body().getContact();
                    /*Intent intent = new Intent(HomeActivity.this,FragmentContainerActivity.class);
                    intent.putExtra("frag","responseContactList");
                    startActivity(intent);
*/              groupListShow();

                }else{
                    Toast.makeText(HomeActivity.this,"Not Successful",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NumberListModel> call, Throwable t) {
                Toast.makeText(HomeActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    //sending all contacts to the server
    public HashMap getAllContacts(){
        hashMap=new HashMap<>();

        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null,null,null,null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                String contact_id =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){
                    Cursor cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{contact_id}, null);
                    while(cursor2.moveToNext()){

                        String phoneNumber=cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        hashMap.put(name,phoneNumber);
                    }
                    cursor2.close();
                }
            }
            cursor.close();
        }
        return hashMap;
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
                          String groupId = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).
                                  itemView.findViewById(R.id.default_group_id)).getText().toString().trim();
                          group_id = Integer.parseInt(groupId);
                          Log.i("GroupId",group_id+"");
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
        Intent intent = new Intent(HomeActivity.this,ContactsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

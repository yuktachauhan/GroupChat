package com.example.android.groupchatapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.groupchatapp.Controller;
import com.example.android.groupchatapp.model.NumberListModel;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;
import com.example.android.groupchatapp.model.ModelLogin;
import com.example.android.groupchatapp.model.ModelToken;
import com.example.android.groupchatapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

   private EditText username,password;
   private static String token;
   private static int user_id;
   String user,pwd;
   Controller mController;
   private ApiInterface apiInterface;
    public static final int REQUEST_READ_CONTACTS=1;
    private static ArrayList<HashMap<String,String>> arrayList;  //for contacts
    HashMap<String,String> hashMap;//for sending all contacts
    SharedPreferences sharedPreferences;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        mController=(Controller) getApplicationContext();
        sharedPreferences=getSharedPreferences("login",0);
    }

    public void doLogin(View view){
        Login();
    }


    public void Login(){
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

         user = username.getText().toString().trim();
         pwd = password.getText().toString().trim();
         SharedPreferences.Editor editor=sharedPreferences.edit();
         editor.putString("username",user);
         editor.putString("password",pwd);
         editor.apply();
         apiInterface= ApiClient.ApiClient().create(ApiInterface.class);

        ModelLogin modelLogin = new ModelLogin(user,pwd);

        mController.setModelLogin(modelLogin);

        Call<ModelLogin> call=apiInterface.Login(mController.getModelLogin());

        call.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putInt("user_id",response.body().getUser_id());
                    editor.apply();
                    /*SharedPreferences myPref = getApplicationContext().getSharedPreferences("my_pref", 0);  //0 means private mode
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putInt("user_id", response.body().getUser_id()).commit();
                    user_id = myPref.getInt("user_id", response.body().getUser_id());*/
                    myToken();

                }else{
                    Toast.makeText(LoginActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

   /* public static int getUser_id(){
        return user_id;
    }*/

    public void myToken() {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        apiInterface = ApiClient.ApiClient().create(ApiInterface.class);

        ModelToken modelToken = new ModelToken(user, pwd);

        mController.setModelToken(modelToken);

        Call<ModelToken> call = apiInterface.Token(mController.getModelToken());

        call.enqueue(new Callback<ModelToken>() {
            @Override
            public void onResponse(Call<ModelToken> call, Response<ModelToken> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                   /* Toast.makeText(LoginActivity.this,response.body().getToken(), Toast.LENGTH_SHORT).show();*/
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("token",response.body().getToken());
                    editor.apply();
                    /*SharedPreferences myPref = getApplicationContext().getSharedPreferences("my_pref", 0);  //0 means private mode
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("token", response.body().getToken()).commit();

                    token = myPref.getString("token",response.body().getToken());*/
                    accessContacts();
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(LoginActivity.this,"Response is not Successful",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ModelToken> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*public static String getToken(){
        return token;
    }*/

    //access contacts permission and send contacts and show response contacts list

    public void accessContacts() {
        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // request the permission
            ActivityCompat.requestPermissions(LoginActivity.this,
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
                    Toast.makeText(LoginActivity.this,"You cannot chat if you deny the permission.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    //send complete contacts list and get
    public void createContacts(){

        ApiInterface apiInterface =ApiClient.ApiClient().create(ApiInterface.class);

        NumberListModel numberListModel = new NumberListModel();
        String token=sharedPreferences.getString("token","");
        Call<NumberListModel> call=apiInterface.createContactList(getAllContacts(),"JWT " + token);
        call.enqueue(new Callback<NumberListModel>() {
            @Override
            public void onResponse(Call<NumberListModel> call, Response<NumberListModel> response) {

                if(response.isSuccessful()){
                    arrayList=new ArrayList<HashMap<String, String>>();
                    arrayList=response.body().getContact();
                }else{
                    Toast.makeText(LoginActivity.this,"Not Successful",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NumberListModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static ArrayList<HashMap<String,String>> getArrayList(){return arrayList;};
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

    //forget password
    public void forgetPassword(View v){
        Intent intent=new Intent(LoginActivity.this,FragmentContainerActivity.class);
        intent.putExtra("frag","forgetEmail");
        startActivity(intent);
    }

    public void SignUpAct(View view){
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

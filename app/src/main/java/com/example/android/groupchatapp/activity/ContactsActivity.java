package com.example.android.groupchatapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.NumberListModel;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity {

    public static final int REQUEST_READ_CONTACTS=1;
    HashMap<String,String> hashMap;
    private static ArrayList<HashMap<String,String>> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        accessContacts();
    }

    public void accessContacts() {
        if (ContextCompat.checkSelfPermission(ContactsActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
                // request the permission
                ActivityCompat.requestPermissions(ContactsActivity.this,
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
                    Toast.makeText(ContactsActivity.this,"You cannot chat if you deny the permission.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void createContacts(){

        final ProgressDialog progressDialog = new ProgressDialog(ContactsActivity.this);
        progressDialog.setMessage("Sending...");
        progressDialog.show();

        ApiInterface apiInterface =ApiClient.ApiClient().create(ApiInterface.class);

        NumberListModel numberListModel=new NumberListModel();


        Call<NumberListModel> call=apiInterface.createContactList(getAllContacts(),"JWT " + LoginActivity.getToken());
        call.enqueue(new Callback<NumberListModel>() {
            @Override
            public void onResponse(Call<NumberListModel> call, Response<NumberListModel> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    arrayList=new ArrayList<HashMap<String, String>>();
                    arrayList=response.body().getContact();
                    Intent intent = new Intent(ContactsActivity.this,FragmentContainerActivity.class);
                    intent.putExtra("frag","responseContactList");
                    startActivity(intent);


                }else{
                    Toast.makeText(ContactsActivity.this,"Not Successful",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NumberListModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ContactsActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static ArrayList<HashMap<String,String>> getArrayList(){return arrayList;};

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
}



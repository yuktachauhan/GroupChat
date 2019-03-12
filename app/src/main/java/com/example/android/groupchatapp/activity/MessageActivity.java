package com.example.android.groupchatapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.groupchatapp.MessageAdapter;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelMessage;
import com.example.android.groupchatapp.model.NumberListModel;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;
    private ArrayList<ModelMessage> messageList;
    private EditText message;
    private CircleImageView sendButton;
    private static int group_id;
    private String groupName;
    private static ArrayList<HashMap<String,String>> member_list=new ArrayList<>();
    static final int  GALLERY_REQUEST_CODE=1;
    Uri imageUri;
    SharedPreferences sharedPreferences;
    private String token;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        sharedPreferences= getSharedPreferences("login",0);
        token=sharedPreferences.getString("token","");

        Intent intent=getIntent();
        group_id=intent.getIntExtra("group_id",0);
        groupName=intent.getStringExtra("group_name");
        member_list=HomeActivity.getMember_list();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(groupName);

        getMessage();
        message=(EditText) findViewById(R.id.message);
        sendButton=(CircleImageView)findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageSend();
                message.setText(" ");
            }
        });

    }

    public static ArrayList<HashMap<String,String>> getMember_list(){
        return member_list;
    }

    public void messageSend(){
        String msg = message.getText().toString().trim();
        ModelMessage modelMessage = new ModelMessage(msg);
        ApiInterface apiInterface= ApiClient.ApiClient().create(ApiInterface.class);

        Call<ModelMessage> call=apiInterface.message(group_id,modelMessage,"JWT "+token);
        call.enqueue(new Callback<ModelMessage>() {
            @Override
            public void onResponse(Call<ModelMessage> call, Response<ModelMessage> response) {
                Toast.makeText(MessageActivity.this, "Message Send", Toast.LENGTH_SHORT).show();
                getMessage();
            }

            @Override
            public void onFailure(Call<ModelMessage> call, Throwable t) {
                Toast.makeText(MessageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getMessage(){
        ApiInterface apiInterface= ApiClient.ApiClient().create(ApiInterface.class);
        Call<ArrayList<ModelMessage>> call=apiInterface.getmessage(group_id,"JWT "+token);
        call.enqueue(new Callback<ArrayList<ModelMessage>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelMessage>> call, Response<ArrayList<ModelMessage>> response) {
                messageList=response.body();
                recyclerView =(RecyclerView) findViewById(R.id.messagelist);
                messageAdapter=new MessageAdapter(messageList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(MessageActivity.this, LinearLayoutManager.VERTICAL));
                recyclerView.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<ModelMessage>> call, Throwable t) {
                Toast.makeText(MessageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.member_menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==android.R.id.home){
            Intent intent =new Intent(getApplicationContext(),HomeActivity.class);
            startActivityForResult(intent,0);
            return true;
        }

        if (id == R.id.add_group_member) {
            Intent intent = new Intent(MessageActivity.this,FragmentContainerActivity.class);
            intent.putExtra("frag","responseContactList");
            startActivity(intent);
            return true;
        }

        if (id == R.id.exit_group) {
            final ProgressDialog progressDialog = new ProgressDialog(MessageActivity.this);
            progressDialog.setMessage("Exiting...");
            progressDialog.show();

            ApiInterface apiInterface=ApiClient.ApiClient().create(ApiInterface.class);
            Call<ResponseBody> call =apiInterface.exitGroup(group_id,"JWT "+LoginActivity.getToken());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        Toast.makeText(MessageActivity.this,"you left the group",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MessageActivity.this,"Some error occurred",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MessageActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        if(id==R.id.update_group){
            Intent intent = new Intent(MessageActivity.this,FragmentContainerActivity.class);
            intent.putExtra("frag","group_update");
            startActivity(intent);
            return true;
        }

        if(id==R.id.group_profile_view){
            Intent intent = new Intent(MessageActivity.this,FragmentContainerActivity.class);
            intent.putExtra("frag","view_group_profile");
            startActivity(intent);
            return true;
        }

        if(id==R.id.member_list){
            Toast.makeText(MessageActivity.this,"Members List",Toast.LENGTH_LONG).show();
            Intent intent =new Intent(MessageActivity.this,FragmentContainerActivity.class);
            intent.putExtra("frag","memberList");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //to select image from gallery

    public void selectImage() {
        Intent pickImage = new Intent(Intent.ACTION_PICK);
        pickImage.setType("image/*");
        if (pickImage.resolveActivity(getPackageManager()) != null) {
            //we check that at least one app is there to perform our action,if no app was there
            startActivityForResult(pickImage, GALLERY_REQUEST_CODE);
            //then because we check our app will not crash
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                imageUri = data.getData();
            }
        }
    }
   public static int getGroup_id(){
        return group_id;
   }

}

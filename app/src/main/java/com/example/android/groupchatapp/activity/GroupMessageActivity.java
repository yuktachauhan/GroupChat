package com.example.android.groupchatapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupMessageActivity extends AppCompatActivity {

    private EditText textMsg;
    private Button sendButton;
    private ListView msgListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        textMsg = (EditText) findViewById(R.id.message);
        sendButton=(Button) findViewById(R.id.msgSendButton);
        msgListView=(ListView) findViewById(R.id.messageListView);
    }
}

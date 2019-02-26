package com.example.android.groupchatapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.android.groupchatapp.MessageAdatpter;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.rest.ApiClient;
import com.example.android.groupchatapp.rest.ApiInterface;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupMessageActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText textMsg;
    private Button sendButton;
    private ListView msgListView;
    private MessageAdatpter messageAdatpter;
    private final List<String> msgList =new ArrayList<>();
    private Socket mSocket;
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);
        textMsg = (EditText) findViewById(R.id.message);
        sendButton=(Button) findViewById(R.id.msgSendButton);
        msgListView=(ListView) findViewById(R.id.messageListView);
        messageAdatpter=new MessageAdatpter(msgList,this);
        msgListView.setAdapter(messageAdatpter);
        sendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String message=textMsg.getText().toString().trim();

    }
}

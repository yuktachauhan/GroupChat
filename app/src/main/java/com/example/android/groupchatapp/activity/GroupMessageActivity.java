package com.example.android.groupchatapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.groupchatapp.MessageAdapter;
import com.example.android.groupchatapp.R;
import com.example.android.groupchatapp.model.ModelMessage;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GroupMessageActivity extends AppCompatActivity {

    private Socket socket;
    public RecyclerView recyclerView;
    public List<ModelMessage> messageList;
    public MessageAdapter messageAdapter;
    public EditText msg;
    public Button msgSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);

        try{
          socket = IO.socket("http://8a9285b2.ngrok.io/");
          socket.connect();
        }catch (Exception e){
            e.printStackTrace();
        }

        msg = (EditText) findViewById(R.id.message) ;
        msgSend = (Button)findViewById(R.id.send);
        messageList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        msgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!msg.getText().toString().isEmpty()){
                    socket.emit("messagedetection",msg.getText().toString());
                    msg.setText(" ");
                }
            }
        });

        //implementing socket listeners
        socket.on("userjoinedthechat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];

                        Toast.makeText(GroupMessageActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Toast.makeText(GroupMessageActivity.this,data,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            //extract data from fired event

                            String message = data.getString("message");

                            // make instance of message

                            ModelMessage m = new ModelMessage(message);


                            //add the message to the messageList

                            messageList.add(m);

                            // add the new updated list to the dapter
                            messageAdapter = new MessageAdapter(messageList);

                            // notify the adapter to update the recycler view

                            messageAdapter.notifyDataSetChanged();

                            //set the adapter for the recycler view

                            recyclerView.setAdapter(messageAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    @Override

    protected void onDestroy() {
        super.onDestroy();

        socket.disconnect();
    }
}


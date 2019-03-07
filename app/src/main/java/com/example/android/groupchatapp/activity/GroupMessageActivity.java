package com.example.android.groupchatapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import okhttp3.OkHttpClient;
import okhttp3.WebSocketListener;

import com.example.android.groupchatapp.R;

public class GroupMessageActivity extends AppCompatActivity{

    private final class EchoWebSocketListener extends WebSocketListener{

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);

}
}
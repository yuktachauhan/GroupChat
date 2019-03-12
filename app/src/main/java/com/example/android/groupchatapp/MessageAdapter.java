package com.example.android.groupchatapp;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.groupchatapp.model.ModelMessage;

import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
private List<ModelMessage> messageList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_message_list, viewGroup, false);

        return new MessageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
           ModelMessage modelMessage= messageList.get(i);
          /* HashMap<String,String> msg=modelMessage.getMessaged_by();
           String name =(String) msg.get("name");*/
          int id =modelMessage.getId();
           myViewHolder.message.setText(modelMessage.getMessage());
           myViewHolder.time.setText(modelMessage.getTime());
           myViewHolder.user_id.setText(modelMessage.getId()+"");
           myViewHolder.msg_by.setText(id+"");
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg_by;
        public TextView time;
        public TextView message;
        public TextView user_id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            msg_by=(TextView) itemView.findViewById(R.id.msg_by);
            message=(TextView) itemView.findViewById(R.id.message);
            time=(TextView) itemView.findViewById(R.id.time);
            user_id=(TextView) itemView.findViewById(R.id.userId);
        }
    }

    public MessageAdapter(List<ModelMessage> list){
        this.messageList=list;
    }
}

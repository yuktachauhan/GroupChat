package com.example.android.groupchatapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {

    private static ClickListener clickListener;
    private ArrayList<HashMap<String,String>> arrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView name,number;

        public MyViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            name = (TextView) view.findViewById(R.id.default_text_member);
            number=(TextView) view.findViewById(R.id.default_text_number);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public ContactListAdapter(ArrayList<HashMap<String,String>> arrayList){
        this.arrayList=arrayList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView =LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.response_contactlist_contents,viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {

           HashMap<String,String> hashMap=arrayList.get(i);
            for(String key : hashMap.keySet()){
                myViewHolder.name.setText(key);
                myViewHolder.number.setText(hashMap.get(key));
            }
        }


    @Override
    public int getItemCount() {
       return arrayList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ContactListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

}

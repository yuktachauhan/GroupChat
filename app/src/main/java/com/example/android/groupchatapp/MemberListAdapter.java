package com.example.android.groupchatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<HashMap<String,String>> member_list;
    private static ClickListener clickListener;

    public MemberListAdapter(Context context,ArrayList<HashMap<String,String>> member_list){
        this.context=context;
        this.member_list=member_list;
    }

    @NonNull
    @Override
    public MemberListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView =LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.response_contactlist_contents,viewGroup, false);

        return new MemberListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberListAdapter.MyViewHolder myViewHolder, int i) {
          HashMap<String,String> member_data=member_list.get(i);
          String name =member_data.get("name");
          String number=member_data.get("phone_number");
          String avatar =member_data.get("avatar");
          myViewHolder.member_name.setText(name);
          myViewHolder.Member_number.setText(number);
          Picasso.with(context).load("http://6a540527.ngrok.io/"+avatar).fit()
                  .placeholder(R.drawable.group).into(myViewHolder.memberImage);
    }

    @Override
    public int getItemCount() {
        return member_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView member_name,Member_number;
        public CircleImageView memberImage;

        public MyViewHolder(@NonNull View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            member_name = (TextView) view.findViewById(R.id.default_text_member);
            Member_number = (TextView) view.findViewById(R.id.default_text_number);
            memberImage=(CircleImageView) view.findViewById(R.id.default_imageView);
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

    public void setOnItemClickListener(ClickListener clickListener) {
        MemberListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

}

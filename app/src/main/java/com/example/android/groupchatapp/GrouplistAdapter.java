package com.example.android.groupchatapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.groupchatapp.activity.HomeActivity;
import com.example.android.groupchatapp.activity.ProfileViewActivity;
import com.example.android.groupchatapp.model.ModelGroupList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GrouplistAdapter extends RecyclerView.Adapter<GrouplistAdapter.MyViewHolder> {

    private static ClickListener clickListener;
    private ArrayList<ModelGroupList> groupLists;
    private Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView groupName;
        public CircleImageView groupImage;


        public MyViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            groupName = (TextView) view.findViewById(R.id.default_text);
            groupImage =(CircleImageView) view.findViewById(R.id.default_imageView);
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

    public GrouplistAdapter(Context context,ArrayList<ModelGroupList> groupLists){
        this.groupLists = groupLists;
        this.context=context;
    }


    @Override
    public GrouplistAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView =LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_recycler_view_content,viewGroup, false);

        return new GrouplistAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GrouplistAdapter.MyViewHolder myViewHolder, int i) {

       ModelGroupList modelGroupList=groupLists.get(i);
       String name=modelGroupList.getName();
       String avatar=modelGroupList.getAvatar();
       myViewHolder.groupName.setText(name);
       Picasso.with(context).load("http://6647c1fe.ngrok.io/"+avatar).fit().placeholder(R.drawable.group).into(myViewHolder.groupImage);
    }


    @Override
    public int getItemCount() {
        return groupLists.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
       GrouplistAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

}

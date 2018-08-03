package com.sesi.chris.animangaquiz.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.model.User;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>{

    private List<User> lstUser;
    private ItemClickListener itemClickListener;

    public FriendsAdapter(){
        lstUser = Collections.emptyList();
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends,parent,false);
        return new FriendViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {

        User user = lstUser.get(position);
        holder.user = user;
        holder.tvUserName.setText(user.getUserName());
        holder.tvNombreUSer.setText(user.getName());
        Picasso.get()
                .load(Constants.URL_BASE+user.getUrlImageUser())
                .into(holder.imgUser);

        holder.itemView.setOnClickListener(v -> {
            if (null != itemClickListener){
                itemClickListener.onItemClick(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lstUser.size();
    }

    public interface ItemClickListener{
        void onItemClick(User user);
    }

    public List<User> getLstUser() {
        return lstUser;
    }

    public void setLstUser(List<User> lstUser) {
        this.lstUser = lstUser;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public static  class FriendViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombreUSer;
        TextView tvUserName;
        ImageView imgUser;
        User user;
        View itemView;

        public FriendViewHolder(View itemView) {
            super(itemView);
            this.tvUserName = itemView.findViewById(R.id.tv_userID);
            this.tvNombreUSer = itemView.findViewById(R.id.tv_nombre);
            this.imgUser = itemView.findViewById(R.id.imgUser);
            this.itemView = itemView;
        }
    }
}

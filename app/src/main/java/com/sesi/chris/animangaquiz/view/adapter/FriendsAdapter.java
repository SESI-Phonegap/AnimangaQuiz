package com.sesi.chris.animangaquiz.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.view.utils.Utils;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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
        if (!user.getUrlImageUser().equals("")) {
       /*     byte[] decodedAvatar = Base64.decode(user.getUrlImageUser(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedAvatar, 0, decodedAvatar.length);*/
            holder.imgUser.setImageBitmap(Utils.base64ToBitmapImage(user.getUrlImageUser()));
        } else {
            holder.imgUser.setImageResource(R.drawable.ic_account_circle_white_48dp);
        }

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
        CircleImageView imgUser;
        User user;
        View itemViewed;

        public FriendViewHolder(View itemView) {
            super(itemView);
            this.tvUserName = itemView.findViewById(R.id.tv_userID);
            this.tvNombreUSer = itemView.findViewById(R.id.tv_nombre);
            this.imgUser = itemView.findViewById(R.id.imgUser);
            this.itemViewed = itemView;
        }
    }
}

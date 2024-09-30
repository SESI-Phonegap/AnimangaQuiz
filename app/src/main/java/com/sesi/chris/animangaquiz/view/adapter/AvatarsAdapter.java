package com.sesi.chris.animangaquiz.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sesi.chris.animangaquiz.BuildConfig;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class AvatarsAdapter extends RecyclerView.Adapter<AvatarsAdapter.AvatarViewHolder> {

    private List<Wallpaper> lstAvatar;
    private ItemClickListener itemClickListener;

    public AvatarsAdapter(){
        lstAvatar = Collections.emptyList();
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avatar,parent,false);
        return new AvatarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        Wallpaper avatar = lstAvatar.get(position);
        holder.avatar = avatar;
        Picasso.get()
                .load(BuildConfig.BASE_URL+holder.avatar.getUrl())
                .into(holder.imgAvatar);

        holder.imgAvatar.setOnClickListener(v -> {
            if (null != itemClickListener){
                itemClickListener.onItemClick(avatar);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstAvatar.size();
    }

    public void setLstAvatar(List<Wallpaper> lstAvatar){
        this.lstAvatar = lstAvatar;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(Wallpaper avatar);
    }

    public static class  AvatarViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAvatar;
        Wallpaper avatar;
        View itemViewed;

        public AvatarViewHolder(View itemView) {
            super(itemView);
            this.imgAvatar = itemView.findViewById(R.id.img_avatar);
            this.itemViewed = itemView;
        }
    }

}

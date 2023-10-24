package com.sesi.chris.animangaquiz.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>{

    private List<Wallpaper> lstWallpaper;
    private ItemClickListener itemClickListener;

    public WallpaperAdapter(){
        this.lstWallpaper = Collections.emptyList();
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallpaper,parent,false);
        return new WallpaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        Wallpaper wallpaper = lstWallpaper.get(position);
        holder.wallpaper = wallpaper;
        holder.tvCosto.setText(String.valueOf(wallpaper.getCosto()));
        String imgUrl = Constants.URL_BASE+"/"+ holder.wallpaper.getUrl();
        Picasso.get()
                .load(imgUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.imgAnime);

        holder.imgAnime.setOnClickListener(v -> {
            if (null != itemClickListener){
                itemClickListener.onItemClick(wallpaper);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstWallpaper.size();
    }

    public void setLstWallpaper(List<Wallpaper> lstWallpaper){
        this.lstWallpaper = lstWallpaper;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(Wallpaper wallpaper);
    }

    public static  class WallpaperViewHolder extends RecyclerView.ViewHolder{
        TextView tvCosto;
        ImageView imgAnime;
        Wallpaper wallpaper;
        View itemViewed;

        public WallpaperViewHolder(View itemView) {
            super(itemView);
            this.tvCosto = itemView.findViewById(R.id.txt_wallpaper_costo);
            this.imgAnime = itemView.findViewById(R.id.img_view_wallpaper);
            this.itemViewed = itemView;
        }
    }
}

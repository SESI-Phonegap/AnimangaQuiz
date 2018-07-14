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

        //No guarda imagen en cache
  /*      GlideApp.with(holder.imgAnime.getContext())
                .load(holder.wallpaper.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgAnime);*/

        Picasso.get()
                .load(Constants.URL_BASE + holder.wallpaper.getUrl())
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
        View itemView;

        public WallpaperViewHolder(View itemView) {
            super(itemView);
            this.tvCosto = itemView.findViewById(R.id.txt_wallpaper_costo);
            this.imgAnime = itemView.findViewById(R.id.img_view_wallpaper);
            this.itemView = itemView;
        }
    }
}

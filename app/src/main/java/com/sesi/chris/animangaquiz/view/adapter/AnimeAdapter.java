package com.sesi.chris.animangaquiz.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private List<Anime> lstAnimes;
    private ItemClickListener itemClickListener;
    public  AnimeAdapter(){
        lstAnimes = Collections.emptyList();
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime,parent,false);
        return new AnimeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder holder, int position) {
        Anime anime = lstAnimes.get(position);
        holder.anime = anime;
        holder.tvAnimeNombre.setText(anime.getName());
        Picasso.get()
                .load(Constants.URL_BASE+"AnimangaBackEnd"+anime.getImgUrl())
                .into(holder.imgAnime);

        holder.itemView.setOnClickListener(v -> {
            if (null != itemClickListener){
                itemClickListener.onItemClick(anime);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstAnimes.size();
    }

    public List<Anime> getLstAnimes(){
        return lstAnimes;
    }

    public void setLstAnimes(List<Anime> lstAnimes){
        this.lstAnimes = lstAnimes;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(Anime anime);
    }

    public static  class AnimeViewHolder extends RecyclerView.ViewHolder{
        TextView tvAnimeNombre;
        ImageView imgAnime;
        Anime anime;
        View itemViewed;

        public AnimeViewHolder(View itemView) {
            super(itemView);
            this.tvAnimeNombre = itemView.findViewById(R.id.txt_anime_name);
            this.imgAnime = itemView.findViewById(R.id.img_view_anime_image);
            this.itemViewed = itemView;
        }
    }
}

package com.sesi.chris.animangaquiz.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.Respuesta;
import java.util.Collections;
import java.util.List;

public class RespuestasAdapter extends RecyclerView.Adapter<RespuestasAdapter.RespuestaViewHolder>{

    private List<Respuesta> lstRespuesta;
    private ItemClickListener itemClickListener;

    public RespuestasAdapter(){
        lstRespuesta = Collections.emptyList();
    }

    @NonNull
    @Override
    public RespuestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_respuestas,parent,false);
        return new RespuestaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RespuestaViewHolder holder, int position) {
        Respuesta respuesta = lstRespuesta.get(position);
        holder.respuesta = respuesta;
        holder.tvRespuesta.setText(respuesta.getRespuesta());
        holder.itemView.setOnClickListener(v -> {
            if (null != itemClickListener){
                itemClickListener.onItemClick(respuesta);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstRespuesta.size();
    }

    public void setLstRespuesta(List<Respuesta> lstRespuesta){
        this.lstRespuesta = lstRespuesta;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(Respuesta respuesta);
    }
    public static  class RespuestaViewHolder extends RecyclerView.ViewHolder{
        TextView tvRespuesta;
        Respuesta respuesta;
        View itemView;

        public RespuestaViewHolder(View itemView) {
            super(itemView);
            this.tvRespuesta = itemView.findViewById(R.id.tv_respuesta);
            this.itemView = itemView;
        }
    }
}

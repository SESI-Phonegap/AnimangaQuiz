package com.sesi.chris.animangaquiz.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sesi.chris.animangaquiz.R;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.Respuesta;

public class RespuestasAdapter {

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

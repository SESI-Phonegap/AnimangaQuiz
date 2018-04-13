package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreguntasRequest {

    @SerializedName("preguntas")
    @Expose
    private List<Preguntas> preguntas = null;

    public List<Preguntas> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Preguntas> preguntas) {
        this.preguntas = preguntas;
    }
}

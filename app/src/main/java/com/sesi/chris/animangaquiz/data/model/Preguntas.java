package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Preguntas {

    @SerializedName("idQuestion")
    @Expose
    private String idQuestion;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("puntos")
    @Expose
    private String puntos;
    @SerializedName("arrayRespuestas")
    @Expose
    private List<Respuesta> respuestas = null;

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public List<Respuesta> getArrayRespuestas() {
        return respuestas;
    }

    public void setArrayRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}

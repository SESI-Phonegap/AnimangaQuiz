package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Score {

    @SerializedName("idScore")
    @Expose
    private String idScore;
    @SerializedName("score")
    @Expose
    private String puntos;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("idAnime")
    @Expose
    private String idAnime;
    @SerializedName("idUser")
    @Expose
    private String idUser;

    public String getIdScore() {
        return idScore;
    }

    public void setIdScore(String idScore) {
        this.idScore = idScore;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(String idAnime) {
        this.idAnime = idAnime;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}

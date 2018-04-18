package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Score {

    @SerializedName("idScore")
    @Expose
    private String idScore;
    @SerializedName("score")
    @Expose
    private String score;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

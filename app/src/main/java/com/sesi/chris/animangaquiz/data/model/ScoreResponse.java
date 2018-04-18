package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScoreResponse {
    @SerializedName("estatus")
    @Expose
    private String estatus;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("score")
    @Expose
    private Score score;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }
}

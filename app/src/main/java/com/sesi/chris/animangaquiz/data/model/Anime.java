package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Anime {

    @SerializedName("idAnime")
    @Expose
    private int idAnime;
    @SerializedName("anime")
    @Expose
    private String anime;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;

    public int getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(int idAnime) {
        this.idAnime = idAnime;
    }

    public String getAnime() {
        return anime;
    }

    public void setAnime(String anime) {
        this.anime = anime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
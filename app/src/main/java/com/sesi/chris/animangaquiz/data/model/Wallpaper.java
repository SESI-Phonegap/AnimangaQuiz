package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wallpaper {

    @SerializedName("idWallpaper")
    @Expose
    private String idWallpaper;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("urlExample")
    @Expose
    private String urlExample;
    @SerializedName("idAnime")
    @Expose
    private String idAnime;
    @SerializedName("costo")
    @Expose
    private int costo;

    public String getIdWallpaper() {
        return idWallpaper;
    }

    public void setIdWallpaper(String idWallpaper) {
        this.idWallpaper = idWallpaper;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlExample() {
        return urlExample;
    }

    public void setUrlExample(String urlExample) {
        this.urlExample = urlExample;
    }

    public String getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(String idAnime) {
        this.idAnime = idAnime;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }
}

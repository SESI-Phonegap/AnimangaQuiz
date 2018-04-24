package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WallpaperResponse {

    @SerializedName("wallpapers")
    @Expose
    private List<Wallpaper> wallpapers = null;

    public List<Wallpaper> getWallpapers() {
        return wallpapers;
    }

    public void setWallpapers(List<Wallpaper> wallpapers) {
        this.wallpapers = wallpapers;
    }
}

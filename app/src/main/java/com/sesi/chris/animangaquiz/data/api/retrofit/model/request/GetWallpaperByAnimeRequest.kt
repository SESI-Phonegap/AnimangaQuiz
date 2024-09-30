package com.sesi.chris.animangaquiz.data.api.retrofit.model.request

import com.google.gson.annotations.SerializedName

data class GetWallpaperByAnimeRequest(
    @SerializedName("email") val email: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("anime") val idAnime: Int
)

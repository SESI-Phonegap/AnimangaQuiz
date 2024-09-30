package com.sesi.chris.animangaquiz.data.api.retrofit.model.request

import com.google.gson.annotations.SerializedName

data class UpdateLevelScoreGemsTotalScoreRequest(
    @SerializedName("email") val email: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("gems") val gems: Int,
    @SerializedName("score") val score: Int,
    @SerializedName("level") val level: Int,
    @SerializedName("iduser") val idUser: Int,
    @SerializedName("anime") val idAnime: Int
)

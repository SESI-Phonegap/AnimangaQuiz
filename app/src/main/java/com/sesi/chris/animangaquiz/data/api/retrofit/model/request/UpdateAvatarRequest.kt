package com.sesi.chris.animangaquiz.data.api.retrofit.model.request

import com.google.gson.annotations.SerializedName

data class UpdateAvatarRequest(
    @SerializedName("email") val email: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("iduser") val idUser: Int,
    @SerializedName("b64Avatar") val avatarBase64: String
)

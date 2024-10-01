package com.sesi.chris.animangaquiz.data.api.retrofit.model.request

import com.google.gson.annotations.SerializedName

data class AddFriendByIdRequest(
    @SerializedName("email") val email: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("idUser") val idUser: Int,
    @SerializedName("idFriend") val idFriend: Int
)

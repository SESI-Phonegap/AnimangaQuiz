package com.sesi.chris.animangaquiz.data.api.retrofit.model.request

import com.google.gson.annotations.SerializedName

data class NewUserRequest(
    @SerializedName("userName") val userName: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("email") val email: String,
    @SerializedName("edad") val edad: Int,
    @SerializedName("genero") val genero: String,
    @SerializedName("pass") val pass: String,
    @SerializedName("userNameFriend") val userNameFriend: String
)
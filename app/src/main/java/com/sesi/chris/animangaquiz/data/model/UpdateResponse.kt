package com.sesi.chris.animangaquiz.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UpdateResponse(
    @SerializedName("estatus") @Expose
    var estatus: String,
    @SerializedName("error")
    @Expose
    var error: String
)

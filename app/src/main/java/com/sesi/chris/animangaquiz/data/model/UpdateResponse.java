package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateResponse {
    @SerializedName("estatus")
    @Expose
    public String estatus;
    @SerializedName("error")
    @Expose
    public String error;


}

package com.sesi.chris.animangaquiz.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class UpdateResponseD {
    @SerializedName("estatus")
    @Expose
    public String estatus;
    @SerializedName("error")
    @Expose
    public String error;
}

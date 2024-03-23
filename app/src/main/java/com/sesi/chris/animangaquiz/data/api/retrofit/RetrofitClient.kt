package com.sesi.chris.animangaquiz.data.api.retrofit

import com.sesi.chris.animangaquiz.data.api.Constants
import com.sesi.chris.animangaquiz.data.model.UpdateResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitClient {

    @POST(Constants.EndPoint.UPDATE_GEMAS)
    @FormUrlEncoded
    fun updateGems(
        @Field(Constants.ParametersBackEnd.USER_NAME) username: String?,
        @Field(Constants.ParametersBackEnd.PASS) pass: String?,
        @Field(Constants.ParametersBackEnd.ID_USER) idUser: Long,
        @Field(Constants.ParametersBackEnd.GEMS) gems: Int
    ): Call<UpdateResponse>

}
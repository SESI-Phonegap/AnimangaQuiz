package com.sesi.chris.animangaquiz.data.datasource

import com.sesi.chris.animangaquiz.data.api.retrofit.RetrofitClient
import com.sesi.chris.animangaquiz.data.dto.UpdateGemsDto
import com.sesi.chris.animangaquiz.data.model.UpdateResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection.HTTP_OK
import javax.inject.Inject

class UserDataSource @Inject constructor(private val client: RetrofitClient) {

    suspend fun updateGems(
        gemsInfo: UpdateGemsDto,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        handler: (error: String?, isUpdated: Boolean) -> Unit
    ) {
        return withContext(dispatcher) {
            client.updateGems(gemsInfo.email, gemsInfo.pass, gemsInfo.idUser, gemsInfo.gems)
                .enqueue(object : Callback<UpdateResponse> {
                    override fun onResponse(
                        call: Call<UpdateResponse>,
                        response: Response<UpdateResponse>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()!!.estatus.toInt() == HTTP_OK) {
                                handler(null, true)
                            } else {
                                handler(response.message(), false)
                            }
                        }
                    }

                    override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                        handler(t.message.toString(), false)
                    }
                })
        }
    }
}
package com.sesi.chris.animangaquiz.data.core.di

import com.sesi.chris.animangaquiz.data.api.Constants
import com.sesi.chris.animangaquiz.data.api.retrofit.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient():OkHttpClient {
        val client = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        val loggingInterceptorHeader = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        loggingInterceptorHeader.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        client.addInterceptor(loggingInterceptor)
        return client.build()
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(retrofit: Retrofit): RetrofitClient {
        return retrofit.create(RetrofitClient::class.java)
    }
}
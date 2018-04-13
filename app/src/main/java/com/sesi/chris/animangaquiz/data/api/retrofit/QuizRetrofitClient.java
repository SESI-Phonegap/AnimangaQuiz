package com.sesi.chris.animangaquiz.data.api.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.AnimeResponseDeserializer;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.LoginResponseDeserializer;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.PreguntasDeserializer;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.Preguntas;


import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizRetrofitClient {

    private QuizRetrofitService quizRetrofitService;

    public QuizRetrofitClient() {
        initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit = retrofitBuilder();
        quizRetrofitService = retrofit.create(getQuizServiceClass());
    }

    private Retrofit retrofitBuilder() {
        return new Retrofit.Builder().baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(getQuizDeserializer()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        client.addInterceptor(loggingInterceptor);
        return client.build();
    }


    private Class<QuizRetrofitService> getQuizServiceClass() {
        return QuizRetrofitService.class;
    }

    private Gson getQuizDeserializer() {
        return new GsonBuilder().registerTypeAdapter(new TypeToken<LoginResponse>() {
        }.getType(), new LoginResponseDeserializer<LoginResponse>())
                .registerTypeAdapter(new TypeToken<List<Anime>>(){}
                .getType(), new AnimeResponseDeserializer<Anime>())
                .registerTypeAdapter(new TypeToken<List<Preguntas>>(){}
                .getType(), new PreguntasDeserializer<Preguntas>())
                .create();
    }

    protected QuizRetrofitService getQuizService() {
        return quizRetrofitService;
    }

}

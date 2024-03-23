package com.sesi.chris.animangaquiz.data.api.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.AnimeResponseDeserializer;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.CheckLevelAndScoreDeserializer;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.LoginResponseDeserializer;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.PreguntasDeserializer;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.UpdateDeserializer;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.UsersDeserializer;
import com.sesi.chris.animangaquiz.data.api.retrofit.deserializer.WallpaperDeserializer;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;


import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Deprecated
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
        HttpLoggingInterceptor loggingInterceptorHeader = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptorHeader.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        client.addInterceptor(loggingInterceptor);
        return client.build();
    }


    private Class<QuizRetrofitService> getQuizServiceClass() {
        return QuizRetrofitService.class;
    }

    private Gson getQuizDeserializer() {
        return new GsonBuilder()
                .registerTypeAdapter(new TypeToken<LoginResponse>() {}
                .getType(), new LoginResponseDeserializer<LoginResponse>())
                .registerTypeAdapter(new TypeToken<List<Anime>>(){}
                .getType(), new AnimeResponseDeserializer<Anime>())
                .registerTypeAdapter(new TypeToken<List<Preguntas>>(){}
                .getType(), new PreguntasDeserializer<Preguntas>())
                .registerTypeAdapter(new TypeToken<ScoreResponse>(){}
                .getType(), new CheckLevelAndScoreDeserializer<ScoreResponse>())
                .registerTypeAdapter(new TypeToken<UpdateResponseD>(){}
                .getType(), new UpdateDeserializer<UpdateResponseD>())
                .registerTypeAdapter(new TypeToken<List<Wallpaper>>(){}
                .getType(),new WallpaperDeserializer<Wallpaper>())
                .registerTypeAdapter(new TypeToken<List<User>>(){}
                .getType(), new UsersDeserializer<User>())
                .create();
    }

    protected QuizRetrofitService getQuizService() {
        return quizRetrofitService;
    }

}

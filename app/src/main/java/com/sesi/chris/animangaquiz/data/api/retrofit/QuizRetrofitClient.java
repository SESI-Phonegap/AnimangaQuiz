package com.sesi.chris.animangaquiz.data.api.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sesi.chris.animangaquiz.data.api.Constants;
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
        quizRetrofitService = retrofit.create(getSpotifyServiceClass());
    }

    private Retrofit retrofitBuilder() {
        return new Retrofit.Builder().baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(getSpotifyDeserializer()))
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


    private Class<QuizRetrofitService> getSpotifyServiceClass() {
        return QuizRetrofitService.class;
    }

    private Gson getSpotifyDeserializer() {
        return new GsonBuilder().registerTypeAdapter(new TypeToken<List<Artist>>() {
        }.getType(), new ArtistsDeserializer<Artist>())
                .registerTypeAdapter(new TypeToken<List<Track>>() {
                }.getType(), new TracksDeserializer<Track>())
                .create();
    }

    protected QuizRetrofitService getSpotifyService() {
        return quizRetrofitService;
    }

}

package com.sesi.chris.animangaquiz.data.api.retrofit;


import android.support.constraint.ConstraintLayout;

import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.Score;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface QuizRetrofitService {

    @POST(Constants.EndPoint.LOGIN_MOBILE)
    @FormUrlEncoded
    Observable<LoginResponse> login(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                    @Field(Constants.ParametersBackEnd.PASSWORD) String pass);

    @POST(Constants.EndPoint.GET_ALL_ANIMES)
    @FormUrlEncoded
    Observable<List<Anime>> getAllAnimes(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                         @Field(Constants.ParametersBackEnd.PASSWORD) String pass);

    @POST(Constants.EndPoint.GET_QUESTIONS_BY_ANIME_AND_LEVEL)
    @FormUrlEncoded
    Observable<List<Preguntas>> getQuestionsByAnimeAndLevel(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                                            @Field(Constants.ParametersBackEnd.PASSWORD) String pass,
                                                            @Field(Constants.ParametersBackEnd.ID_ANIME) int idAnime,
                                                            @Field(Constants.ParametersBackEnd.LEVEL) int level);

    @POST(Constants.EndPoint.CHECK_LEVEL_AND_SCORE_BY_ANIME_AND_USER)
    @FormUrlEncoded
    Observable<ScoreResponse> checkLevelAndScore(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                                 @Field(Constants.ParametersBackEnd.PASSWORD) String pass,
                                                 @Field(Constants.ParametersBackEnd.ID_ANIME) int idAnime,
                                                 @Field(Constants.ParametersBackEnd.ID_USER) int idUser);
}

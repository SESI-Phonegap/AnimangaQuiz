package com.sesi.chris.animangaquiz.data.api.retrofit;


import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;

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
}

package com.sesi.chris.animangaquiz.data.api.retrofit;


import com.sesi.chris.animangaquiz.data.model.LoginResponse;

import java.util.List;
import java.util.Observable;

import retrofit2.http.POST;

import static com.sesi.chris.animangaquiz.data.api.Constants.EndPoint.LOGIN_MOBILE;

public interface QuizRetrofitService {

    @POST(LOGIN_MOBILE)
    Observable<List<LoginResponse>> login();
}

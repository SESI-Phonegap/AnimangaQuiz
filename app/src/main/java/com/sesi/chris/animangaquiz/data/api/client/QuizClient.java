package com.sesi.chris.animangaquiz.data.api.client;


import com.sesi.chris.animangaquiz.data.api.retrofit.QuizRetrofitClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import retrofit2.http.Body;

public class QuizClient extends QuizRetrofitClient implements QuizServiceClient{
    @Override
    public Observable<List<LoginResponse>> login(RequestBody json) {
        return getQuizService().login(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

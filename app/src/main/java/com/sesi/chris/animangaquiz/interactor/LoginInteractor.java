package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;

import java.util.List;


import io.reactivex.Observable;
import okhttp3.RequestBody;

public class LoginInteractor {

    private QuizServiceClient quizServiceClient;

    public LoginInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<LoginResponse>> login(RequestBody requestBody){
        return quizServiceClient.login(requestBody);
    }
}

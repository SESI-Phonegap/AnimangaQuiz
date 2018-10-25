package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;

import java.util.List;

import io.reactivex.Observable;

public class LoginInteractor {

    private QuizServiceClient quizServiceClient;

    public LoginInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<LoginResponse> login(String userName, String pass){
        return quizServiceClient.login(userName,pass);
    }

    public Observable<UpdateResponse> updateGems(String userName, String pass, int idUser, int gemas ){
        return quizServiceClient.updateGemas(userName,pass,idUser,gemas);
    }

    public Observable<List<Wallpaper>> getAvatarByUser(String userName, String pass, int idUser){
        return quizServiceClient.getAvatarsByUser(userName,pass,idUser);
    }

}

package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.AnimeResponse;

import java.util.List;

import io.reactivex.Observable;

public class MenuInteractor {

    private QuizServiceClient quizServiceClient;

    public MenuInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Anime>> animes(String userName, String pass){
        return quizServiceClient.getAllAnimes(userName,pass);
    }
}

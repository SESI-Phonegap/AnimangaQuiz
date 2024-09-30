package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;
import java.util.List;
import io.reactivex.Observable;

public class MenuInteractor {

    private QuizServiceClient quizServiceClient;

    public MenuInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Anime>> animes(Credentials request){
        return quizServiceClient.getAllAnimes(request);
    }

    public Observable<List<Anime>> getAnimesImg(String userName, String pass){
        return quizServiceClient.getAllAnimesImg(userName, pass);
    }

    public Observable<ScoreResponse> checkScoreAndLevel(String userName, String pass, int idAnime, int idUser){
        return quizServiceClient.checkScoreAndLevel(userName,pass,idAnime,idUser);
    }

}

package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.Preguntas;

import java.util.List;

import io.reactivex.Observable;

public class PreguntasInteractor {
    private QuizServiceClient quizServiceClient;

    public PreguntasInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Preguntas>> preguntasByAnimeAndLevel(String userName, String pass, int idAnime, int level){
        return quizServiceClient.getQuestionsByAnimeAndLevel(userName,pass,idAnime,level);
    }
}

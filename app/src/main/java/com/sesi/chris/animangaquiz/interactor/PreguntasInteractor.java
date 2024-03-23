package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;

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

    public Observable<UpdateResponseD> updateLevelScoreGems(String userName, String pass, int gemas, int score, int level, int idUser,
                                                            int idAnime){
        return quizServiceClient.updateLevelScoreGems(userName,pass,gemas,score,level,idUser,idAnime);
    }

    public Observable<UpdateResponseD> updateEsferas(String userName, String pass, int idUser, int esferas){
        return quizServiceClient.updateEsferas(userName,pass,idUser,esferas);
    }
}

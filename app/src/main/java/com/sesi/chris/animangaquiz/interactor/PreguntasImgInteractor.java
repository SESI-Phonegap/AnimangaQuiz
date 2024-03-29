package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;

import java.util.List;

import io.reactivex.Observable;

public class PreguntasImgInteractor {
    private QuizServiceClient quizServiceClient;

    public PreguntasImgInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Preguntas>> preguntasByAnimeImg(String userName, String pass, int idAnime){
        return quizServiceClient.getQuestionsByAnimeImg(userName,pass,idAnime);
    }

    public Observable<UpdateResponseD> updateEsferas(String userName, String pass, int idUser, int esferas){
        return quizServiceClient.updateEsferas(userName,pass,idUser,esferas);
    }

    public Observable<UpdateResponseD> updateLevelScoreGems(String userName, String pass, int gemas, int score, int level, int idUser,
                                                            int idAnime){
        return quizServiceClient.updateLevelScoreGems(userName,pass,gemas,score,level,idUser,idAnime);
    }
}

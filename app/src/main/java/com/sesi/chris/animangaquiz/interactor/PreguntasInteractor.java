package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.QuestionByAnimeLevelRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateEsferasRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateLevelScoreGemsTotalScoreRequest;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;

import java.util.List;

import io.reactivex.Observable;

public class PreguntasInteractor {
    private QuizServiceClient quizServiceClient;

    public PreguntasInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Preguntas>> preguntasByAnimeAndLevel(QuestionByAnimeLevelRequest request){
        return quizServiceClient.getQuestionsByAnimeAndLevel(request);
    }

    public Observable<UpdateResponseD> updateLevelScoreGems(UpdateLevelScoreGemsTotalScoreRequest request){
        return quizServiceClient.updateLevelScoreGems(request);
    }

    public Observable<UpdateResponseD> updateEsferas(UpdateEsferasRequest request){
        return quizServiceClient.updateEsferas(request);
    }
}

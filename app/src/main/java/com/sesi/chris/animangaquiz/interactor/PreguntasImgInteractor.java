package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetQuestionsByAnimeImgRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateEsferasRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateLevelScoreGemsTotalScoreRequest;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;

import java.util.List;

import io.reactivex.Observable;

public class PreguntasImgInteractor {
    private QuizServiceClient quizServiceClient;

    public PreguntasImgInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Preguntas>> preguntasByAnimeImg(GetQuestionsByAnimeImgRequest request){
        return quizServiceClient.getQuestionsByAnimeImg(request);
    }

    public Observable<UpdateResponseD> updateEsferas(UpdateEsferasRequest request){
        return quizServiceClient.updateEsferas(request);
    }

    public Observable<UpdateResponseD> updateLevelScoreGems(UpdateLevelScoreGemsTotalScoreRequest request){
        return quizServiceClient.updateLevelScoreGems(request);
    }
}

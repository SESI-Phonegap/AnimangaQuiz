package com.sesi.chris.animangaquiz.data.api.client;

import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.Score;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;

import java.util.List;

import io.reactivex.Observable;

public interface QuizServiceClient {
    Observable<LoginResponse> login(String userName, String pass);
    Observable<List<Anime>> getAllAnimes(String userName, String pass);
    Observable<List<Preguntas>> getQuestionsByAnimeAndLevel(String userName, String pass, int idAnime, int level);
    Observable<ScoreResponse> checkScoreAndLevel(String userName, String pass, int idAnime, int idUser);
}

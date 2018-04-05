package com.sesi.chris.animangaquiz.data.api.client;

import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;

import java.util.List;

import io.reactivex.Observable;

public interface QuizServiceClient {
    Observable<LoginResponse> login(String userName, String pass);
    Observable<List<Anime>> getAllAnimes(String userName, String pass);
}

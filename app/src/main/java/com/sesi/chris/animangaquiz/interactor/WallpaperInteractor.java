package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;

import java.util.List;

import io.reactivex.Observable;

public class WallpaperInteractor {
    private QuizServiceClient quizServiceClient;

    public WallpaperInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Anime>> getAnimes(String userName, String pass){
        return quizServiceClient.getAllAnimes(userName,pass);
    }

    public Observable<List<Wallpaper>> getWallpaperByAnime(String userName, String pass, int idAnime){
        return quizServiceClient.getWallpaperByAnime(userName,pass,idAnime);
    }
}

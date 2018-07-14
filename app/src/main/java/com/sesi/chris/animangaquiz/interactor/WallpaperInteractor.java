package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;

import java.util.List;

import io.reactivex.Observable;

public class WallpaperInteractor {
    private QuizServiceClient quizServiceClient;

    public WallpaperInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Anime>> getAnimesForWallpaper(String userName, String pass){
        return quizServiceClient.getAllAnimesForWallpaper(userName,pass);
    }

    public Observable<List<Wallpaper>> getWallpaperByAnime(String userName, String pass, int idAnime){
        return quizServiceClient.getWallpaperByAnime(userName,pass,idAnime);
    }

    public Observable<UpdateResponse> updateGemas(String userName, String pass, int idUser, int gemas){
        return quizServiceClient.updateGemas(userName,pass,idUser,gemas);
    }
}

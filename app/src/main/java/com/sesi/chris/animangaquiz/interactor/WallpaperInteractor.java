package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetWallpaperByAnimeRequest;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;

import java.util.List;

import io.reactivex.Observable;

public class WallpaperInteractor {
    private QuizServiceClient quizServiceClient;

    public WallpaperInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<Anime>> getAnimesForWallpaper(Credentials request){
        return quizServiceClient.getAllAnimesForWallpaper(request);
    }

    public Observable<List<Wallpaper>> getWallpaperByAnime(GetWallpaperByAnimeRequest request){
        return quizServiceClient.getWallpaperByAnime(request);
    }

    public Observable<UpdateResponseD> updateGemas(String userName, String pass, int idUser, int gemas){
        return quizServiceClient.updateGemas(userName,pass,idUser,gemas);
    }

    public Observable<List<Wallpaper>> getAvatarsByAnime(String userName, String pass, int idAnime){
        return quizServiceClient.getAvatarsByAnime(userName,pass,idAnime);
    }
}

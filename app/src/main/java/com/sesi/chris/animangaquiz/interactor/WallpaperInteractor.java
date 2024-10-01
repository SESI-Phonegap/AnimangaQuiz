package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetAvatarsByAnimeRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetWallpaperByAnimeRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateGemasRequest;
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

    public Observable<UpdateResponseD> updateGemas(UpdateGemasRequest request){
        return quizServiceClient.updateGemas(request);
    }

    public Observable<List<Wallpaper>> getAvatarsByAnime(GetAvatarsByAnimeRequest request){
        return quizServiceClient.getAvatarsByAnime(request);
    }
}

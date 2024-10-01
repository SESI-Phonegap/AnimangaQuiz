package com.sesi.chris.animangaquiz.presenter;

import android.util.Log;

import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetAvatarsByAnimeRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetWallpaperByAnimeRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateGemasRequest;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;
import com.sesi.chris.animangaquiz.interactor.WallpaperInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class WallpaperPresenter extends Presenter<WallpaperPresenter.ViewWallpaper> {

    private WallpaperInteractor wallpaperInteractor;

    public WallpaperPresenter(WallpaperInteractor wallpaperInteractor){
        this.wallpaperInteractor = wallpaperInteractor;
    }

    public void getAllAnimes(String userName, String pass){
        getView().showLoading();
        Credentials request = new Credentials(userName,pass);
        Disposable disposable = wallpaperInteractor.getAnimesForWallpaper(request)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(animes -> {
            if (!animes.isEmpty()){
                getView().hideLoading();
                getView().renderAnimes(animes);
            } else {
                getView().showAnimesNotFoundMessage();
            }
        },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void getWallpaperByAnime(String userName, String pass, int idAnime){
        getView().showLoading();
        GetWallpaperByAnimeRequest request = new GetWallpaperByAnimeRequest(userName,pass,idAnime);
        Disposable disposable = wallpaperInteractor.getWallpaperByAnime(request)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(lstWallpaper -> {
            if (!lstWallpaper.isEmpty()){
                getView().hideLoading();
                getView().renderWallpaperByAnimes(lstWallpaper);
            } else {
                getView().showAnimesNotFoundMessage();
            }
        },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void getAvatarsByAnime(String userName, String pass, int idAnime){
        GetAvatarsByAnimeRequest request = new GetAvatarsByAnimeRequest(userName,pass,idAnime);
        getView().showLoading();
        Disposable disposable = wallpaperInteractor.getAvatarsByAnime(request)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(lstAvatar -> {
                    if (!lstAvatar.isEmpty()){
                        getView().hideLoading();
                        getView().renderAvatarsByAnime(lstAvatar);
                    } else {
                        getView().hideLoading();
                        getView().showAnimesNotFoundMessage();
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void updateGemas(String userName, String pass, int idUser, int gemas){
        getView().showLoading();
        UpdateGemasRequest request = new UpdateGemasRequest(userName,pass,idUser,gemas);
        Disposable disposable = wallpaperInteractor.updateGemas(request)
                .doOnError(error -> Log.e("Error-",error.getMessage()))
                .subscribe(updateResponse -> {
                    if (null != updateResponse){
                        getView().hideLoading();
                        getView().renderUpdateGemas(updateResponse);
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void launchWallpaperAnime(Anime anime){
        getView().launchWallpaperByanime(anime);
    }

    public void launchAvatarByAnime(Anime anime){
        getView().launchAvatarByAnime(anime);
    }

    public interface ViewWallpaper extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showAnimesNotFoundMessage();

        void showServerError(String error);

        void renderAnimes(List<Anime> lstAnimes);

        void renderWallpaperByAnimes(List<Wallpaper> lstWallpaper);

        void renderAvatarsByAnime(List<Wallpaper> lstAvatar);

        void launchWallpaperByanime(Anime anime);

        void launchAvatarByAnime(Anime anime);

        void renderUpdateGemas(UpdateResponseD updateResponse);

    }
}

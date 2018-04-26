package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;
import com.sesi.chris.animangaquiz.interactor.WallpaperInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class WallpaperPresenter extends Presenter<WallpaperPresenter.View> {

    private WallpaperInteractor wallpaperInteractor;

    public WallpaperPresenter(WallpaperInteractor wallpaperInteractor){
        this.wallpaperInteractor = wallpaperInteractor;
    }

    public void getAllAnimes(String userName, String pass){
        getView().showLoading();
        Disposable disposable = wallpaperInteractor.getAnimes(userName,pass)
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
        Disposable disposable = wallpaperInteractor.getWallpaperByAnime(userName,pass,idAnime)
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

    public void updateGemas(String userName, String pass, int idUser, int gemas){
        getView().showLoading();
        Disposable disposable = wallpaperInteractor.updateGemas(userName,pass,idUser,gemas)
                .doOnError(error -> error.printStackTrace())
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

    public interface View extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showAnimesNotFoundMessage();

        void showServerError(String error);

        void renderAnimes(List<Anime> lstAnimes);

        void renderWallpaperByAnimes(List<Wallpaper> lstWallpaper);

        void launchWallpaperByanime(Anime anime);

        void renderUpdateGemas(UpdateResponse updateResponse);

    }
}

package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;
import com.sesi.chris.animangaquiz.interactor.MenuInteractor;
import java.util.List;
import io.reactivex.disposables.Disposable;

public class MenuPresenter extends Presenter<MenuPresenter.ViewMenu>{

    private MenuInteractor interactor;

    public MenuPresenter(MenuInteractor interactor){
        this.interactor = interactor;
    }

    public void getAllAnimes(Credentials request){
        getView().showLoading();
        Disposable disposable = interactor.animes(request)
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

    public void getAllAnimesImg(String userName, String pass){
        getView().showLoading();
        Disposable disposable = interactor.getAnimesImg(userName, pass)
                .doOnError(error -> {
                    getView().hideLoading();
                    getView().showServerError(error.getMessage());
                }).subscribe(anime -> {
                    getView().hideLoading();
                    if (!anime.isEmpty()){
                        getView().renderAnimes(anime);
                    } else {
                        getView().showAnimesNotFoundMessage();
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void checkScoreAndLevel(String userName, String pass, int idAnime, int idUser){
        getView().showLoading();
        Disposable disposable = interactor.checkScoreAndLevel(userName,pass,idAnime,idUser)
                .doOnError(error ->
                getView().showServerError(error.getMessage())
                )
                .subscribe(score -> {
            if (null != score){
                getView().hideLoading();
                getView().renderScoreAndLevel(score);
            } else {
                getView().showScoreError();
            }
        },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }


    public void launchAnimeTest(Anime anime){
        getView().launchAnimeTest(anime);
    }

    public interface ViewMenu extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showAnimesNotFoundMessage();

        void showServerError(String error);

        void renderAnimes(List<Anime> lstAnimes);

        void renderScoreAndLevel(ScoreResponse score);

        void showScoreError();

        void launchAnimeTest(Anime anime);

    }
}

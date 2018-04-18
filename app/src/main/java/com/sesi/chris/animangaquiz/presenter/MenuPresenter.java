package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.Score;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;
import com.sesi.chris.animangaquiz.interactor.MenuInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class MenuPresenter extends Presenter<MenuPresenter.View>{

    private MenuInteractor interactor;

    public MenuPresenter(MenuInteractor interactor){
        this.interactor = interactor;
    }

    public void getAllAnimes(String userName, String pass){
        getView().showLoading();
        Disposable disposable = interactor.animes(userName,pass).subscribe(animes -> {
            if (!animes.isEmpty()){
                getView().hideLoading();
                getView().renderAnimes(animes);
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

    public interface View extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showAnimesNotFoundMessage();

        void showConnectionErrorMessage();

        void showServerError(String Error);

        void renderAnimes(List<Anime> lstAnimes);

        void renderScoreAndLevel(ScoreResponse score);

        void showScoreError();

        void launchAnimeTest(Anime anime);
    }
}

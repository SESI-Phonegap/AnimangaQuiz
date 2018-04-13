package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.Anime;
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

    public void launchAnimeTest(Anime anime){
        getView().launchAnimeTest(anime);
    }

    public interface View extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showAnimesNotFoundMessage();

        void showConnectionErrorMessage();

        void showServerError();

        void renderAnimes(List<Anime> lstAnimes);

        void launchAnimeTest(Anime anime);
    }
}

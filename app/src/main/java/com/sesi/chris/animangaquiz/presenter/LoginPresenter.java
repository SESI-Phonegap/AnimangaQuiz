package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;

import java.util.List;

import io.reactivex.disposables.Disposable;


public class LoginPresenter extends Presenter<LoginPresenter.View> {

    private LoginInteractor interactor;

    public LoginPresenter(LoginInteractor interactor) {
        this.interactor = interactor;
    }

    public void onLogin(String userName, String password) {
        getView().showLoading();

        Disposable disposable = interactor.login(userName, password)
                .doOnError(error ->
                        getView().showServerError(error.getMessage()))
                .subscribe(login -> {
                    if (null != login) {
                        getView().hideLoading();
                        getView().renderLogin(login);
                    } else {
                        getView().showLoginNotFoundMessage();
                    }
                }, Throwable::printStackTrace);
        addDisposableObserver(disposable);

    }

    public void onUpdateGems(String userName, String password, int idUser, int gemas){
        Disposable disposable = interactor.updateGems(userName,password,idUser,gemas)
                .doOnError(error ->
                        getView().showServerError(error.getMessage()))
                .subscribe(updateResponse -> {
                    if (null != updateResponse){
                        getView().hideLoading();
                        getView().updateGemsResponse(updateResponse);
                    } else {
                        getView().showUpdateGemsError();
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);

    }


    @Override
    public void terminate() {
        super.terminate();
        setView(null);
    }

    public interface View extends Presenter.View {
        void showLoading();

        void hideLoading();

        void showLoginNotFoundMessage();

        void showServerError(String error);

        void renderLogin(LoginResponse loginResponse);

        void updateGemsResponse(UpdateResponse updateResponse);

        void showUpdateGemsError();

    }
}

package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;

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
    }
}

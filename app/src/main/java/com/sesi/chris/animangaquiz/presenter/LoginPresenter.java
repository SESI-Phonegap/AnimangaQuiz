package com.sesi.chris.animangaquiz.presenter;


import android.util.Log;

import com.google.gson.Gson;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.User;

import java.util.List;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPresenter extends Presenter<LoginPresenter.View> {

    private LoginInteractor interactor;

    public LoginPresenter(LoginInteractor interactor){
        this.interactor = interactor;
    }

    public void onLogin(String userName, String password){
        getView().showLoading();

        Disposable disposable = interactor.login(userName,password).subscribe(login -> {
            if (null != login){
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

    public interface View extends Presenter.View{
        void showLoading();

        void hideLoading();

        void showLoginNotFoundMessage();

        void showConnectionErrorMessage();

        void showServerError();

        void renderLogin(LoginResponse loginResponse);
    }
}

package com.sesi.chris.animangaquiz.presenter;


import com.google.gson.Gson;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.User;

import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginPresenter extends Presenter<LoginPresenter.View> {

    private LoginInteractor interactor;

    public LoginPresenter(LoginInteractor interactor){
        this.interactor = interactor;
    }

    public void onLogin(String userName, String password){
        getView().showLoading();
        Disposable disposable = interactor.login(getLoginRequestBody(userName,password)).subscribe(login -> {
            if (!login.isEmpty() && login.size() > 0){
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

    public RequestBody getLoginRequestBody(String userName, String password){
        Gson gson = new Gson();
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
       return RequestBody.create(MediaType.parse("application/json"),gson.toJson(user));
    }

    public interface View extends Presenter.View{
        void showLoading();

        void hideLoading();

        void showLoginNotFoundMessage();

        void showConnectionErrorMessage();

        void showServerError();

        void renderLogin(List<LoginResponse> posts);
    }
}

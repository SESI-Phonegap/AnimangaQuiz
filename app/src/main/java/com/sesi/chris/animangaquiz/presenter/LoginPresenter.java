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
      /*  Call<LoginResponse> callLogin = interactor.login(userName,password);
        callLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d("LOGINPRESENTER--","No hay conexion");
                } else {

                        User user = response.body().getUser();
                        Log.d("LOGINPRESENTER--", response.body().getEstatus());

                        if (null != user) {
                            Log.d("LOGINPRESENTER--", user.getName());
                        } else {
                            Log.d("", response.body().getError());
                        }

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });*/

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
/*
    public RequestBody getLoginRequestBody(String userName, String password){
        Gson gson = new Gson();
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        String json = gson.toJson(user);
       return RequestBody.create(MediaType.parse("application/json"),gson.toJson(user));
    }*/

    public interface View extends Presenter.View{
        void showLoading();

        void hideLoading();

        void showLoginNotFoundMessage();

        void showConnectionErrorMessage();

        void showServerError();

        void renderLogin(LoginResponse loginResponse);
    }
}

package com.sesi.chris.animangaquiz.presenter;

import android.util.Log;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.interactor.RegistroNuevoUsuarioInteractor;
import io.reactivex.disposables.Disposable;

public class RegistroNuevoUsuarioPresenter extends Presenter<RegistroNuevoUsuarioPresenter.ViewRegistro> {

    private RegistroNuevoUsuarioInteractor interactor;

    public RegistroNuevoUsuarioPresenter(RegistroNuevoUsuarioInteractor interactor){
        this.interactor = interactor;
    }

    public void registroNuevoUsuario(String userNameFriend, String username,String nombre,String email,int edad,String genero,String password){
        getView().showLoading();
        Disposable disposable = interactor.registroNuevoUsuario(userNameFriend,username,nombre,email,edad,genero,password)
                .doOnError(error -> {
                    Log.e("Error-",error.getMessage());
                    getView().hideLoading();
                })
                .subscribe(updateResponse -> {
                    if (null != updateResponse) {
                        getView().hideLoading();
                        getView().renderResponse(updateResponse);
                    }

                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void onLogin(String userName, String password){
        getView().showLoading();
        Disposable disposable = interactor.login(userName,password).doOnError(error -> getView().showServerError(error.getMessage())).subscribe(login -> {
            if (null != login){
                getView().hideLoading();
                getView().renderLogin(login);
            } else {
                getView().showLoginNotFoundMessage();
            }
        }, Throwable::printStackTrace);
        addDisposableObserver(disposable);

    }

    public interface ViewRegistro extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showLoginNotFoundMessage();

        void showServerError(String error);

        void renderResponse(UpdateResponse updateResponse);

        void renderLogin(LoginResponse loginResponse);
    }
}

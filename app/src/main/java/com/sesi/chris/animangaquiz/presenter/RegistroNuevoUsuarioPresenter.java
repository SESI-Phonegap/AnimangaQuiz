package com.sesi.chris.animangaquiz.presenter;

import android.util.Log;

import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.interactor.RegistroNuevoUsuarioInteractor;
import io.reactivex.disposables.Disposable;

public class RegistroNuevoUsuarioPresenter extends Presenter<RegistroNuevoUsuarioPresenter.ViewRegistro> {

    private RegistroNuevoUsuarioInteractor interactor;

    public RegistroNuevoUsuarioPresenter(RegistroNuevoUsuarioInteractor interactor){
        this.interactor = interactor;
    }

    public void registroNuevoUsuario(NewUserRequest request){
        getView().showLoading();
        Disposable disposable = interactor.registroNuevoUsuario(request)
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

    public void onLogin(String email, String password){
        getView().showLoading();
        Credentials request = new Credentials(email,password);
        Disposable disposable = interactor.login(request).doOnError(error -> getView().showServerError(error.getMessage())).subscribe(login -> {
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

        void renderResponse(UpdateResponseD updateResponse);

        void renderLogin(LoginResponse loginResponse);
    }
}

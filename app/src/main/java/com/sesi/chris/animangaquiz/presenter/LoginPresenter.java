package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.UpdateResponse;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import io.reactivex.disposables.Disposable;

public class LoginPresenter extends Presenter<LoginPresenter.ViewLogin> {

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
                        getView().updateGemsResponse(updateResponse);
                        getView().hideLoading();
                    } else {
                        getView().showUpdateGemsError();
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);

    }

    public void onUpdateEsferas(String userName, String pass, int idUser, int esferas ){
        getView().showLoading();
        Disposable disposable = interactor.updateEsferas(userName, pass, idUser, esferas)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(updateResponse -> {
                    if (null != updateResponse){
                        getView().hideLoading();
                        getView().updateGemsResponse(updateResponse);
                    } else {
                        getView().showServerError("Ocurrio un error");
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void onUpdateAvatar(String userName, String pass, int idUser, String b64){
        getView().showLoading();
        Disposable disposable = interactor.updateAvatar(userName,pass,idUser,b64)
                .doOnError(error -> {
                    getView().showServerError(error.getMessage());
                    getView().hideLoading();
                }).subscribe(updateResponse -> {
                    getView().hideLoading();
                    if (null != updateResponse){
                        getView().updateAvatarResponse(updateResponse);
                    } else {
                        getView().showUpdateAvatarError();
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void onRegisterNewUser(String userNameFriend, String userName,String nombre,String email,int edad,String genero,String password){
       getView().showLoading();
       Disposable disposable = interactor.registroNuevoUsuario(userNameFriend,userName,nombre,email,edad,genero,password)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(updateResponse -> {
                    if (null != updateResponse){
                        getView().hideLoading();
                        getView().renderResponse(updateResponse);
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void onRegisterNewUserFacebook(String userNameFriend, String userName,String nombre,String email,int edad,String genero,String password){
        getView().showLoading();
        Disposable disposable = interactor.registroNuevoUsuario(userNameFriend,userName,nombre,email,edad,genero,password)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(updateResponse -> {
                    if (null != updateResponse){
                        getView().hideLoading();
                        getView().renderResponseFacebook(updateResponse);
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void validaUsuarioFacebook(String userName){
        getView().showLoading();
        Disposable disposable = interactor.validaUsuarioFacebook(userName)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(loginResponse -> {
                    if (null != loginResponse){
                        getView().hideLoading();
                        getView().renderLoginFacbook(loginResponse);
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    @Override
    public void terminate() {
        super.terminate();
        setView(null);
    }

    public interface ViewLogin extends Presenter.View {
        void showLoading();

        void hideLoading();

        void showLoginNotFoundMessage();

        void showServerError(String error);

        void renderLogin(LoginResponse loginResponse);

        void updateGemsResponse(UpdateResponse updateResponse);

        void showUpdateGemsError();

        void updateAvatarResponse(UpdateResponse updateResponse);

        void showUpdateAvatarError();

        void renderResponse(UpdateResponse updateResponse);

        void renderResponseFacebook(UpdateResponse updateResponse);

        void renderLoginFacbook(LoginResponse loginResponse);

    }
}

package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.DeleteUserRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateAvatarRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateEsferasRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateGemasRequest;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.interactor.LoginInteractor;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import io.reactivex.disposables.Disposable;

public class LoginPresenter extends Presenter<LoginPresenter.ViewLogin> {

    private LoginInteractor interactor;

    public LoginPresenter(LoginInteractor interactor) {
        this.interactor = interactor;
    }

    public void onLogin(Credentials request) {
        getView().showLoading();
        Disposable disposable = interactor.login(request)
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
        UpdateGemasRequest request = new UpdateGemasRequest(userName,password,idUser,gemas);
        Disposable disposable = interactor.updateGems(request)
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
        UpdateEsferasRequest request = new UpdateEsferasRequest(userName,pass,idUser,esferas);
        Disposable disposable = interactor.updateEsferas(request)
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
        UpdateAvatarRequest request = new UpdateAvatarRequest(userName,pass,idUser,b64);
        Disposable disposable = interactor.updateAvatar(request)
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

    public void onRegisterNewUser(NewUserRequest request){
       getView().showLoading();
       Disposable disposable = interactor.registroNuevoUsuario(request)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(updateResponse -> {
                    if (null != updateResponse){
                        getView().hideLoading();
                        getView().renderResponse(updateResponse);
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void onRegisterNewUserFacebook(NewUserRequest request){
        getView().showLoading();
        Disposable disposable = interactor.registroNuevoUsuario(request)
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

    public void deleteUser(String email, String pass, int idUser){
        DeleteUserRequest request = new DeleteUserRequest(email,pass,idUser);
        getView().showLoading();
        Disposable disposable = interactor.deleteUser(request)
                .doOnError(error -> getView().showServerError(error.getMessage()))
                .subscribe(deleteUserResponse -> {
                    getView().hideLoading();
                    if (deleteUserResponse != null) {
                        getView().deleteUserAction();
                    }
                }, Throwable::printStackTrace);
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

        void updateGemsResponse(UpdateResponseD updateResponse);

        void showUpdateGemsError();

        void updateAvatarResponse(UpdateResponseD updateResponse);

        void showUpdateAvatarError();

        void renderResponse(UpdateResponseD updateResponse);

        void renderResponseFacebook(UpdateResponseD updateResponse);

        void renderLoginFacbook(LoginResponse loginResponse);
        void deleteUserAction();

    }
}

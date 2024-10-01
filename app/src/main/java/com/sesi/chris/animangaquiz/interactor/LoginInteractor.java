package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.DeleteUserRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateAvatarRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateEsferasRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateGemasRequest;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;

import io.reactivex.Observable;

public class LoginInteractor {

    private QuizServiceClient quizServiceClient;

    public LoginInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<LoginResponse> login(Credentials request){
        return quizServiceClient.login(request);
    }

    public Observable<UpdateResponseD> updateGems(UpdateGemasRequest request ){
        return quizServiceClient.updateGemas(request);
    }

    public Observable<UpdateResponseD> updateAvatar(UpdateAvatarRequest request){
        return quizServiceClient.updateAvatar(request);
    }

    public Observable<UpdateResponseD> registroNuevoUsuario(NewUserRequest request){
        return quizServiceClient.registroNuevoUsuario(request);
    }

    public Observable<LoginResponse> validaUsuarioFacebook(String userName){
        return quizServiceClient.validaUsuarioFacebook(userName);
    }

    public Observable<UpdateResponseD> updateEsferas(UpdateEsferasRequest request){
        return quizServiceClient.updateEsferas(request);
    }

    public Observable<UpdateResponseD> deleteUser(DeleteUserRequest request) {
        return quizServiceClient.deleteUser(request);
    }

}

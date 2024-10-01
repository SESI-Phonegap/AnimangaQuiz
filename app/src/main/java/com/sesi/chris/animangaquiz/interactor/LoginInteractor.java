package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
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

    public Observable<UpdateResponseD> updateAvatar(String userName, String pass, int idUser, String b64){
        return quizServiceClient.updateAvatar(userName,pass,idUser,b64);
    }

    public Observable<UpdateResponseD> registroNuevoUsuario(NewUserRequest request){
        return quizServiceClient.registroNuevoUsuario(request);
    }

    public Observable<LoginResponse> validaUsuarioFacebook(String userName){
        return quizServiceClient.validaUsuarioFacebook(userName);
    }

    public Observable<UpdateResponseD> updateEsferas(String userName, String pass, int idUser, int esferas ){
        return quizServiceClient.updateEsferas(userName,pass,idUser,esferas);
    }

    public Observable<UpdateResponseD> deleteUser(String email, String pass, int idUser) {
        return quizServiceClient.deleteUser(email, pass, idUser);
    }

}

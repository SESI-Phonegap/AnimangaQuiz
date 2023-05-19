package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;

import io.reactivex.Observable;

public class LoginInteractor {

    private QuizServiceClient quizServiceClient;

    public LoginInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<LoginResponse> login(String email, String pass){
        return quizServiceClient.login(email,pass);
    }

    public Observable<UpdateResponse> updateGems(String userName, String pass, int idUser, int gemas ){
        return quizServiceClient.updateGemas(userName,pass,idUser,gemas);
    }

    public Observable<UpdateResponse> updateAvatar(String userName,String pass, int idUser, String b64){
        return quizServiceClient.updateAvatar(userName,pass,idUser,b64);
    }

    public Observable<UpdateResponse> registroNuevoUsuario(String userNameFriend,String username,String nombre,String email,int edad,String genero,String password){
        return quizServiceClient.registroNuevoUsuario(userNameFriend,username,nombre,email,edad,genero,password);
    }

    public Observable<LoginResponse> validaUsuarioFacebook(String userName){
        return quizServiceClient.validaUsuarioFacebook(userName);
    }

    public Observable<UpdateResponse> updateEsferas(String userName, String pass, int idUser, int esferas ){
        return quizServiceClient.updateEsferas(userName,pass,idUser,esferas);
    }

}

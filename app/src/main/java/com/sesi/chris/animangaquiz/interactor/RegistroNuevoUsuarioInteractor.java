package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;

import io.reactivex.Observable;

public class RegistroNuevoUsuarioInteractor {

    private QuizServiceClient quizServiceClient;

    public RegistroNuevoUsuarioInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<UpdateResponseD> registroNuevoUsuario(String userNameFriend, String username, String nombre, String email, int edad, String genero, String password){
        return quizServiceClient.registroNuevoUsuario(userNameFriend,username,nombre,email,edad,genero,password);
    }

    public Observable<LoginResponse> login(String email, String pass){
        return quizServiceClient.login(email,pass);
    }
}

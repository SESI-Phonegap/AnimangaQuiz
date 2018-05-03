package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponse;

import io.reactivex.Observable;

public class RegistroNuevoUsuarioInteractor {

    private QuizServiceClient quizServiceClient;

    public RegistroNuevoUsuarioInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<UpdateResponse> registroNuevoUsuario(String username,String nombre,String email,int edad,String genero,String password){
        return quizServiceClient.registroNuevoUsuario(username,nombre,email,edad,genero,password);
    }

    public Observable<LoginResponse> login(String userName, String pass){
        return quizServiceClient.login(userName,pass);
    }
}

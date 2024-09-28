package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;

import io.reactivex.Observable;

public class RegistroNuevoUsuarioInteractor {

    private QuizServiceClient quizServiceClient;

    public RegistroNuevoUsuarioInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<UpdateResponseD> registroNuevoUsuario(NewUserRequest request){
        return quizServiceClient.registroNuevoUsuario(request);
    }

    public Observable<LoginResponse> login(Credentials request){
        return quizServiceClient.login(request);
    }
}

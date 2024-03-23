package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.data.model.User;

import java.util.List;

import io.reactivex.Observable;

public class FriendsInteractor {
    private QuizServiceClient quizServiceClient;

    public FriendsInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<User>> searchFriendsByUserName(String userName, String pass, String userNameQuery){
        return quizServiceClient.searchFriendByUserName(userName,pass,userNameQuery);
    }

    public Observable<UpdateResponseD> addFriendById(String userName, String pass, int idUser, int idFriend){
        return quizServiceClient.addFrienById(userName,pass,idUser,idFriend);
    }
}

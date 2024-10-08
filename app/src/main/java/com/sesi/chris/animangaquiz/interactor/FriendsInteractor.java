package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.AddFriendByIdRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.SearchFriendByUserNameRequest;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.data.model.User;

import java.util.List;

import io.reactivex.Observable;

public class FriendsInteractor {
    private QuizServiceClient quizServiceClient;

    public FriendsInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<User>> searchFriendsByUserName(SearchFriendByUserNameRequest request){
        return quizServiceClient.searchFriendByUserName(request);
    }

    public Observable<UpdateResponseD> addFriendById(AddFriendByIdRequest request){
        return quizServiceClient.addFrienById(request);
    }
}

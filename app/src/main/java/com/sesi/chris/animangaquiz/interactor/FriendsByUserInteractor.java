package com.sesi.chris.animangaquiz.interactor;

import com.sesi.chris.animangaquiz.data.api.client.QuizServiceClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.model.User;
import java.util.List;
import io.reactivex.Observable;


public class FriendsByUserInteractor {
    private QuizServiceClient quizServiceClient;

    public FriendsByUserInteractor(QuizServiceClient quizServiceClient){
        this.quizServiceClient = quizServiceClient;
    }

    public Observable<List<User>> getAllFriendsByUser(Credentials request){
        return quizServiceClient.getAllFriendsByUser(request);
    }

}

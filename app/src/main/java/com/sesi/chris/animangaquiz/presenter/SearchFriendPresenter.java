package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.FriendsInteractor;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class SearchFriendPresenter extends Presenter<SearchFriendPresenter.View> {

    private FriendsInteractor interactor;

    public SearchFriendPresenter(FriendsInteractor interactor){
        this.interactor = interactor;
    }

    public void searchFriend(String userName, String pass, String userNameQuery){
        getView().showLoading();
        Disposable disposable = interactor.searchFriendsByUserName(userName,pass,userNameQuery)
                .doOnError(error->{
                    getView().showServerError(error.getMessage());
                    getView().hideLoading();
                }).subscribe(friends -> {
                    if (!friends.isEmpty()){
                        getView().hideLoading();
                        getView().renderFriends(friends);
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void addFriend(String userName){
        getView().addFriend(userName);
    }

    public interface View extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showServerError(String error);

        void renderFriends(List<User> lstUser);

        void addFriend(String userName);

    }
}

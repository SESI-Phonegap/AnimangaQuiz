package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.AddFriendByIdRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.SearchFriendByUserNameRequest;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.FriendsInteractor;
import java.util.List;
import io.reactivex.disposables.Disposable;

public class SearchFriendPresenter extends Presenter<SearchFriendPresenter.ViewSearchFriend> {

    private FriendsInteractor interactor;

    public SearchFriendPresenter(FriendsInteractor interactor){
        this.interactor = interactor;
    }

    public void searchFriend(String userName, String pass, String userNameQuery){
        SearchFriendByUserNameRequest request = new SearchFriendByUserNameRequest(userName, pass, userNameQuery);
        getView().showLoading();
        Disposable disposable = interactor.searchFriendsByUserName(request)
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

    public void addFriend(String userName, String pass, int iIdUser, int iIdFriend){
        AddFriendByIdRequest request = new AddFriendByIdRequest(userName,pass,iIdUser,iIdFriend);
        getView().showLoading();
        Disposable disposable = interactor.addFriendById(request)
                .doOnError(error -> {
                    getView().showServerError(error.getMessage());
                    getView().hideLoading();
                }).subscribe(updateResponse -> {
                    if (null != updateResponse){
                        getView().hideLoading();
                        getView().renderAddFriend(updateResponse);
                    }
                },Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public interface ViewSearchFriend extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showServerError(String error);

        void renderFriends(List<User> lstUser);

        void renderAddFriend(UpdateResponseD updateResponse);

    }
}

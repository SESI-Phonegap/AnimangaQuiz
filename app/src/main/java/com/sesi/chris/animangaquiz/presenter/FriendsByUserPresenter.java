package com.sesi.chris.animangaquiz.presenter;

import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.interactor.FriendsByUserInteractor;
import java.util.List;
import io.reactivex.disposables.Disposable;

public class FriendsByUserPresenter extends Presenter<FriendsByUserPresenter.ViewFriendsByUser> {

    private FriendsByUserInteractor interactor;

    public FriendsByUserPresenter(FriendsByUserInteractor interactor) {
        this.interactor = interactor;
    }

    public void getAllFriendsByUser(String userName, String pass) {
        getView().showLoading();
        Disposable disposable = interactor.getAllFriendsByUser(userName, pass)
                .doOnError(error -> {
                    getView().showServerError(error.getMessage());
                    getView().hideLoading();
                }).subscribe(friends -> {
                    if (!friends.isEmpty()) {
                        getView().hideLoading();
                        getView().renderFriends(friends);
                    } else {
                        getView().showFriendsNotFoundMessage();
                    }
                }, Throwable::printStackTrace);
        addDisposableObserver(disposable);
    }

    public void clickFriend(User user) {
        //Metodo en construccion
    }

    @Override
    public void terminate() {
        super.terminate();
        setView(null);
    }

    public interface ViewFriendsByUser extends Presenter.View {

        void showLoading();

        void hideLoading();

        void showFriendsNotFoundMessage();

        void showServerError(String error);

        void renderFriends(List<User> lstUser);

        void clickFirend(User user);

    }

}

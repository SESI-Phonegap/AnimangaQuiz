package com.sesi.chris.animangaquiz.data.api.client;


import com.sesi.chris.animangaquiz.data.api.retrofit.QuizRetrofitClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
import com.sesi.chris.animangaquiz.data.model.Anime;
import com.sesi.chris.animangaquiz.data.model.LoginResponse;
import com.sesi.chris.animangaquiz.data.model.Preguntas;
import com.sesi.chris.animangaquiz.data.model.ScoreResponse;
import com.sesi.chris.animangaquiz.data.model.UpdateResponseD;
import com.sesi.chris.animangaquiz.data.model.User;
import com.sesi.chris.animangaquiz.data.model.Wallpaper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class QuizClient extends QuizRetrofitClient implements QuizServiceClient {
    @Override
    public Observable<LoginResponse> login(Credentials request) {
        return getQuizService().login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Anime>> getAllAnimes(String userName, String pass) {
        return getQuizService().getAllAnimes(userName, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Anime>> getAllAnimesImg(String userName, String pass) {
        return getQuizService().getAllAnimesImg(userName, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Anime>> getAllAnimesForWallpaper(String userName, String pass) {
        return getQuizService().getAllanimesForWallpaper(userName, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Preguntas>> getQuestionsByAnimeAndLevel(String userName, String pass, int idAnime, int level) {
        return getQuizService().getQuestionsByAnimeAndLevel(userName, pass, idAnime, level)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ScoreResponse> checkScoreAndLevel(String userName, String pass, int idAnime, int idUser) {
        return getQuizService().checkLevelAndScore(userName, pass, idAnime, idUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> updateLevelScoreGems(String userName, String pass, int gemas, int score, int level, int idUser, int idAnime) {
        return getQuizService().updateLevelScoreGemsTotalScore(userName, pass, gemas, score, level, idUser, idAnime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Wallpaper>> getWallpaperByAnime(String userName, String pass, int idAnime) {
        return getQuizService().getWallpaperByAnime(userName, pass, idAnime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> updateGemas(String userName, String pass, int idUser, int gemas) {
        return getQuizService().updateGemas(userName, pass, idUser, gemas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> registroNuevoUsuario(NewUserRequest request) {
        return getQuizService().registroNuevoUsuario(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<User>> searchFriendByUserName(String userName, String pass, String userNameQuery) {
        return getQuizService().searchFriendByUserName(userName, pass, userNameQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> addFrienById(String userName, String pass, int idUser, int idFriend) {
        return getQuizService().addFriendById(userName, pass, idUser, idFriend)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Wallpaper>> getAvatarsByAnime(String userName, String pass, int idAnime) {
        return getQuizService().getAvatarsByAnime(userName, pass, idAnime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<User>> getAllFriendsByUser(String userName, String pass) {
        return getQuizService().getAllFriendsByUser(userName, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> updateAvatar(String userName, String pass, int idUser, String b64) {
        return getQuizService().updateAvatar(userName, pass, idUser, b64)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<LoginResponse> validaUsuarioFacebook(String userName) {
        return getQuizService().validaUsuarioFacebook(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> updateEsferas(String userName, String pass, int idUser, int esferas) {
        return getQuizService().updateEsferas(userName, pass, idUser, esferas)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Preguntas>> getQuestionsByAnimeImg(String userName, String pass, int idAnime) {
        return getQuizService().getQuestionsByAnimeImg(userName, pass, idAnime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> deleteUser(String email, String pass, int userId) {
        return getQuizService().deleteUser(email, pass, userId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}

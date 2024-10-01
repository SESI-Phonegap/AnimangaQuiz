package com.sesi.chris.animangaquiz.data.api.client;


import com.sesi.chris.animangaquiz.data.api.retrofit.QuizRetrofitClient;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.AddFriendByIdRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.CheckLevelAndScoreRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetAvatarsByAnimeRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetWallpaperByAnimeRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.QuestionByAnimeLevelRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.SearchFriendByUserNameRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateAvatarRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateEsferasRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateGemasRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.UpdateLevelScoreGemsTotalScoreRequest;
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
    public Observable<List<Anime>> getAllAnimes(Credentials request) {
        return getQuizService().getAllAnimes(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Anime>> getAllAnimesImg(Credentials request) {
        return getQuizService().getAllAnimesImg(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Anime>> getAllAnimesForWallpaper(Credentials request) {
        return getQuizService().getAllanimesForWallpaper(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Preguntas>> getQuestionsByAnimeAndLevel(QuestionByAnimeLevelRequest request) {
        return getQuizService().getQuestionsByAnimeAndLevel(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ScoreResponse> checkScoreAndLevel(CheckLevelAndScoreRequest request) {
        return getQuizService().checkLevelAndScore(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> updateLevelScoreGems(UpdateLevelScoreGemsTotalScoreRequest request) {
        return getQuizService().updateLevelScoreGemsTotalScore(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Wallpaper>> getWallpaperByAnime(GetWallpaperByAnimeRequest request) {
        return getQuizService().getWallpaperByAnime(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> updateGemas(UpdateGemasRequest request) {
        return getQuizService().updateGemas(request)
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
    public Observable<List<User>> searchFriendByUserName(SearchFriendByUserNameRequest request) {
        return getQuizService().searchFriendByUserName(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> addFrienById(AddFriendByIdRequest request) {
        return getQuizService().addFriendById(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Wallpaper>> getAvatarsByAnime(GetAvatarsByAnimeRequest request) {
        return getQuizService().getAvatarsByAnime(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<User>> getAllFriendsByUser(Credentials request) {
        return getQuizService().getAllFriendsByUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UpdateResponseD> updateAvatar(UpdateAvatarRequest request) {
        return getQuizService().updateAvatar(request)
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
    public Observable<UpdateResponseD> updateEsferas(UpdateEsferasRequest request) {
        return getQuizService().updateEsferas(request)
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

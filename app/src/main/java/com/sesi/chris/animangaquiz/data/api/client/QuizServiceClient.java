package com.sesi.chris.animangaquiz.data.api.client;

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

public interface QuizServiceClient {
    Observable<LoginResponse> login(Credentials request);
    Observable<List<Anime>> getAllAnimes(Credentials request);
    Observable<List<Anime>> getAllAnimesImg(Credentials request);
    Observable<List<Anime>> getAllAnimesForWallpaper(Credentials request);
    Observable<List<Preguntas>> getQuestionsByAnimeAndLevel(QuestionByAnimeLevelRequest request);
    Observable<ScoreResponse> checkScoreAndLevel(CheckLevelAndScoreRequest request);
    Observable<UpdateResponseD> updateLevelScoreGems(UpdateLevelScoreGemsTotalScoreRequest request);
    Observable<List<Wallpaper>> getWallpaperByAnime(GetWallpaperByAnimeRequest request);
    Observable<UpdateResponseD> updateGemas(UpdateGemasRequest request);
    Observable<UpdateResponseD> registroNuevoUsuario(NewUserRequest request);
    Observable<List<User>> searchFriendByUserName(SearchFriendByUserNameRequest request);
    Observable<UpdateResponseD> addFrienById(AddFriendByIdRequest request);
    Observable<List<Wallpaper>> getAvatarsByAnime(GetAvatarsByAnimeRequest request);
    Observable<List<User>> getAllFriendsByUser(Credentials request);
    Observable<UpdateResponseD> updateAvatar(UpdateAvatarRequest request);
    Observable<LoginResponse> validaUsuarioFacebook(String userName);
    Observable<UpdateResponseD> updateEsferas(UpdateEsferasRequest request);
    Observable<List<Preguntas>> getQuestionsByAnimeImg(String userName, String pass, int idAnime);
    Observable<UpdateResponseD> deleteUser(String email,
                                           String pass,
                                           int userId);
}

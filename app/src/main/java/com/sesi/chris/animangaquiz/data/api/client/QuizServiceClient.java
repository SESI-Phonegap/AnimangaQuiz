package com.sesi.chris.animangaquiz.data.api.client;

import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.QuestionByAnimeLevelRequest;
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
    Observable<ScoreResponse> checkScoreAndLevel(String userName, String pass, int idAnime, int idUser);
    Observable<UpdateResponseD> updateLevelScoreGems(String userName, String pass,
                                                     int gemas, int score,
                                                     int level, int idUser,
                                                     int idAnime);
    Observable<List<Wallpaper>> getWallpaperByAnime(String userName, String pass, int idAnime);
    Observable<UpdateResponseD> updateGemas(String userName, String pass, int idUser, int gemas);
    Observable<UpdateResponseD> registroNuevoUsuario(NewUserRequest request);
    Observable<List<User>> searchFriendByUserName(String userName, String pass, String userNameQuery);
    Observable<UpdateResponseD> addFrienById(String userName, String pass, int idUser, int idFriend);
    Observable<List<Wallpaper>> getAvatarsByAnime(String userName, String pass, int idAnime);
    Observable<List<User>> getAllFriendsByUser(String userName, String pass);
    Observable<UpdateResponseD> updateAvatar(String userName, String pass, int idUser, String b64);
    Observable<LoginResponse> validaUsuarioFacebook(String userName);
    Observable<UpdateResponseD> updateEsferas(String userName, String pass, int idUser, int esferas);
    Observable<List<Preguntas>> getQuestionsByAnimeImg(String userName, String pass, int idAnime);
    Observable<UpdateResponseD> deleteUser(String email,
                                           String pass,
                                           int userId);
}

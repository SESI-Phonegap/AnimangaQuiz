package com.sesi.chris.animangaquiz.data.api.client;

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
    Observable<LoginResponse> login(String email, String pass);
    Observable<List<Anime>> getAllAnimes(String userName, String pass);
    Observable<List<Anime>> getAllAnimesImg(String userName, String pass);
    Observable<List<Anime>> getAllAnimesForWallpaper(String userName, String pass);
    Observable<List<Preguntas>> getQuestionsByAnimeAndLevel(String userName, String pass, int idAnime, int level);
    Observable<ScoreResponse> checkScoreAndLevel(String userName, String pass, int idAnime, int idUser);
    Observable<UpdateResponseD> updateLevelScoreGems(String userName, String pass,
                                                     int gemas, int score,
                                                     int level, int idUser,
                                                     int idAnime);
    Observable<List<Wallpaper>> getWallpaperByAnime(String userName, String pass, int idAnime);
    Observable<UpdateResponseD> updateGemas(String userName, String pass, int idUser, int gemas);
    Observable<UpdateResponseD> registroNuevoUsuario(String userNameFriend, String username, String nombre, String email, int edad, String genero, String password);
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

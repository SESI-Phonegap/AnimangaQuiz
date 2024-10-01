package com.sesi.chris.animangaquiz.data.api.retrofit;

import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.CheckLevelAndScoreRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetWallpaperByAnimeRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.NewUserRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.QuestionByAnimeLevelRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.SearchFriendByUserNameRequest;
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
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface QuizRetrofitService {

    @POST(Constants.EndPoint.LOGIN_MOBILE)
    Observable<LoginResponse> login(@Body Credentials request);

    @POST(Constants.EndPoint.GET_ALL_ANIMES)
    Observable<List<Anime>> getAllAnimes(@Body Credentials request);

    @POST(Constants.EndPoint.GET_ALL_ANIMES_IMG)
    @FormUrlEncoded
    Observable<List<Anime>> getAllAnimesImg(@Body Credentials request);

    @POST(Constants.EndPoint.GET_ALL_ANIMES_FOR_WALLPAPER)
    @FormUrlEncoded
    Observable<List<Anime>> getAllanimesForWallpaper(@Body Credentials request);

    @POST(Constants.EndPoint.GET_QUESTIONS_BY_ANIME_AND_LEVEL)
    @FormUrlEncoded
    Observable<List<Preguntas>> getQuestionsByAnimeAndLevel(@Body QuestionByAnimeLevelRequest request);

    @POST(Constants.EndPoint.CHECK_LEVEL_AND_SCORE_BY_ANIME_AND_USER)
    @FormUrlEncoded
    Observable<ScoreResponse> checkLevelAndScore(@Body CheckLevelAndScoreRequest request);

    @POST(Constants.EndPoint.UPDATE_LEVEL_AND_SCORE)
    @FormUrlEncoded
    Observable<UpdateResponseD> updateLevelScoreGemsTotalScore(@Body UpdateLevelScoreGemsTotalScoreRequest request);

    @POST(Constants.EndPoint.GET_WALLPAPER_BY_ANIME)
    @FormUrlEncoded
    Observable<List<Wallpaper>> getWallpaperByAnime(@Body GetWallpaperByAnimeRequest request);

    @POST(Constants.EndPoint.UPDATE_GEMAS)
    @FormUrlEncoded
    Observable<UpdateResponseD> updateGemas(@Body UpdateGemasRequest request);

    @POST(Constants.EndPoint.REGISTRO_NUEVO_USUARIO)
    Observable<UpdateResponseD> registroNuevoUsuario(@Body NewUserRequest request);

    @POST(Constants.EndPoint.SEARCH_FRIEND_BY_USER_NAME)
    @FormUrlEncoded
    Observable<List<User>> searchFriendByUserName(@Body SearchFriendByUserNameRequest request);

    @POST(Constants.EndPoint.ADD_FRIEND_BY_ID)
    @FormUrlEncoded
    Observable<UpdateResponseD> addFriendById(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                              @Field(Constants.ParametersBackEnd.PASS) String pass,
                                              @Field(Constants.ParametersBackEnd.ID_USER) int idUser,
                                              @Field(Constants.ParametersBackEnd.ID_FRIEND) int idFriend);

    @POST(Constants.EndPoint.GET_AVATARS_BY_ANIME)
    @FormUrlEncoded
    Observable<List<Wallpaper>> getAvatarsByAnime(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                                  @Field(Constants.ParametersBackEnd.PASS) String pass,
                                                  @Field(Constants.ParametersBackEnd.ID_ANIME) int idAnime);

    @POST(Constants.EndPoint.GET_ALL_FRIENDS_BY_USER)
    @FormUrlEncoded
    Observable<List<User>> getAllFriendsByUser(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                               @Field(Constants.ParametersBackEnd.PASS) String pass);

    @POST(Constants.EndPoint.UPDATE_AVATAR)
    @FormUrlEncoded
    Observable<UpdateResponseD> updateAvatar(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                             @Field(Constants.ParametersBackEnd.PASS) String pass,
                                             @Field(Constants.ParametersBackEnd.ID_USER)int idUser,
                                             @Field(Constants.ParametersBackEnd.AVATAR_BASE64)String b64);

    @POST(Constants.EndPoint.CHECK_USER_FACEBOOK)
    @FormUrlEncoded
    Observable<LoginResponse> validaUsuarioFacebook(@Field(Constants.ParametersBackEnd.USER_NAME) String username);

    @POST(Constants.EndPoint.UPDATE_ESFERAS)
    @FormUrlEncoded
    Observable<UpdateResponseD> updateEsferas(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                              @Field(Constants.ParametersBackEnd.PASS) String pass,
                                              @Field(Constants.ParametersBackEnd.ID_USER) int idUser,
                                              @Field(Constants.ParametersBackEnd.ESFERAS) int esferas);

    @POST(Constants.EndPoint.GET_QUESTIONS_BY_ANIME_IMG)
    @FormUrlEncoded
    Observable<List<Preguntas>> getQuestionsByAnimeImg(@Field(Constants.ParametersBackEnd.USER_NAME) String username,
                                                            @Field(Constants.ParametersBackEnd.PASS) String pass,
                                                            @Field(Constants.ParametersBackEnd.ID_ANIME) int idAnime);
    @POST(Constants.EndPoint.DELETE_USER)
    @FormUrlEncoded
    Observable<UpdateResponseD> deleteUser(@Field(Constants.ParametersBackEnd.EMAIL) String email,
                                           @Field(Constants.ParametersBackEnd.PASS) String pass,
                                           @Field(Constants.ParametersBackEnd.ID_USER) int userId);
    }

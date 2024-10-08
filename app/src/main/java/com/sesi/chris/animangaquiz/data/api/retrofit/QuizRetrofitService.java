package com.sesi.chris.animangaquiz.data.api.retrofit;

import static com.sesi.chris.animangaquiz.data.api.retrofit.Constants.HEADER_ACCEPT_JSON;
import static com.sesi.chris.animangaquiz.data.api.retrofit.Constants.HEADER_CONTENT_JSON;

import com.sesi.chris.animangaquiz.data.api.Constants;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.AddFriendByIdRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.CheckLevelAndScoreRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.Credentials;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.DeleteUserRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetAvatarsByAnimeRequest;
import com.sesi.chris.animangaquiz.data.api.retrofit.model.request.GetQuestionsByAnimeImgRequest;
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
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface QuizRetrofitService {

    @POST(Constants.EndPoint.LOGIN_MOBILE)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<LoginResponse> login(@Body Credentials request);

    @POST(Constants.EndPoint.GET_ALL_ANIMES)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<Anime>> getAllAnimes(@Body Credentials request);

    @POST(Constants.EndPoint.GET_ALL_ANIMES_IMG)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<Anime>> getAllAnimesImg(@Body Credentials request);

    @POST(Constants.EndPoint.GET_ALL_ANIMES_FOR_WALLPAPER)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<Anime>> getAllanimesForWallpaper(@Body Credentials request);

    @POST(Constants.EndPoint.GET_QUESTIONS_BY_ANIME_AND_LEVEL)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<Preguntas>> getQuestionsByAnimeAndLevel(@Body QuestionByAnimeLevelRequest request);

    @POST(Constants.EndPoint.CHECK_LEVEL_AND_SCORE_BY_ANIME_AND_USER)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<ScoreResponse> checkLevelAndScore(@Body CheckLevelAndScoreRequest request);

    @POST(Constants.EndPoint.UPDATE_LEVEL_AND_SCORE)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<UpdateResponseD> updateLevelScoreGemsTotalScore(@Body UpdateLevelScoreGemsTotalScoreRequest request);

    @POST(Constants.EndPoint.GET_WALLPAPER_BY_ANIME)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<Wallpaper>> getWallpaperByAnime(@Body GetWallpaperByAnimeRequest request);

    @POST(Constants.EndPoint.UPDATE_GEMAS)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<UpdateResponseD> updateGemas(@Body UpdateGemasRequest request);

    @POST(Constants.EndPoint.REGISTRO_NUEVO_USUARIO)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<UpdateResponseD> registroNuevoUsuario(@Body NewUserRequest request);

    @POST(Constants.EndPoint.SEARCH_FRIEND_BY_USER_NAME)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<User>> searchFriendByUserName(@Body SearchFriendByUserNameRequest request);

    @POST(Constants.EndPoint.ADD_FRIEND_BY_ID)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<UpdateResponseD> addFriendById(@Body AddFriendByIdRequest request);

    @POST(Constants.EndPoint.GET_AVATARS_BY_ANIME)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<Wallpaper>> getAvatarsByAnime(@Body GetAvatarsByAnimeRequest request);

    @POST(Constants.EndPoint.GET_ALL_FRIENDS_BY_USER)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<User>> getAllFriendsByUser(@Body Credentials request);

    @POST(Constants.EndPoint.UPDATE_AVATAR)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<UpdateResponseD> updateAvatar(@Body UpdateAvatarRequest request);

    @POST(Constants.EndPoint.CHECK_USER_FACEBOOK)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<LoginResponse> validaUsuarioFacebook(@Field(Constants.ParametersBackEnd.USER_NAME) String username);

    @POST(Constants.EndPoint.UPDATE_ESFERAS)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<UpdateResponseD> updateEsferas(@Body UpdateEsferasRequest request);

    @POST(Constants.EndPoint.GET_QUESTIONS_BY_ANIME_IMG)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<List<Preguntas>> getQuestionsByAnimeImg(@Body GetQuestionsByAnimeImgRequest request);

    @POST(Constants.EndPoint.DELETE_USER)
    @Headers({HEADER_ACCEPT_JSON, HEADER_CONTENT_JSON})
    Observable<UpdateResponseD> deleteUser(@Body DeleteUserRequest request);
    }

package com.sesi.chris.animangaquiz.data.api;


public class Constants {

    //URL Produccion
    public static final String URL_BASE = "http://www.animangaquiz.mx/";
    // URL WAMP Server localhost Desarrollo
    //public static final String URL_BASE = "http://9.86.173.110:80/";

    // URL MAMP Server localhost Desarrollo
  //  public static final String URL_BASE = "http://192.168.0.7:8888/";


    public static final class EndPoint{
        public static final String LOGIN_MOBILE = "/AnimangaBackEnd/service/loginmobile.php";
        public static final String GET_ALL_ANIMES = "/AnimangaBackEnd/service/getanimes.php";
        public static final String GET_ALL_ANIMES_FOR_WALLPAPER = "/AnimangaBackEnd/service/getallanimesforwallpaper.php";
        public static final String GET_QUESTIONS_BY_ANIME_AND_LEVEL = "/AnimangaBackEnd/service/preguntasByAnimeByLeve.php";
        public static final String CHECK_LEVEL_AND_SCORE_BY_ANIME_AND_USER = "/AnimangaBackEnd/service/checkLevelAndScoreByAnimeAndUser.php";
        public static final String UPDATE_LEVEL_AND_SCORE = "/AnimangaBackEnd/service/updateScoreAndGems.php";
        public static final String GET_WALLPAPER_BY_ANIME = "/AnimangaBackEnd/service/getWallpaperByAnime.php";
        public static final String UPDATE_GEMAS = "/AnimangaBackEnd/service/updateGems.php";
        public static final String REGISTRO_NUEVO_USUARIO = "/AnimangaBackEnd/service/registroNuevoUsuario.php";
        public static final String SEARCH_FRIEND_BY_USER_NAME = "/AnimangaBackEnd/service/searchFriendByUserName.php";
        public static final String ADD_FRIEND_BY_ID = "/AnimangaBackEnd/service/addFriendById.php";
        public static final String GET_AVATARS_BY_ANIME = "/AnimangaBackEnd/service/getAvatarsByAnime.php";
        public static final String GET_ALL_FRIENDS_BY_USER = "/AnimangaBackEnd/service/getAllFriendsByUser.php";
    }

    public static final class ParametersBackEnd{
        public static final String USER_NAME = "userName";
        public static final String PASSWORD = "pass";
        public static final String ID_ANIME = "anime";
        public static final String LEVEL = "level";
        public static final String ID_USER = "iduser";
        public static final String GEMS = "gems";
        public static final String SCORE = "score";
        public static final String GENERO = "genero";
        public static final String EDAD = "edad";
        public static final String EMAIL = "email";
        public static final String NOMBRE = "nombre";
        public static final String USER_NAME_QUERY = "userNameQuery";
        public static final String ID_FRIEND = "idFriend";

    }

    public static final class Deserializer{
        public static final String ANIMES = "animes";
        public static final String PREGUNTAS = "preguntas";
        public static final String WALPAPERS = "wallpapers";
        public static final String FRIENDS = "friends";

    }
}

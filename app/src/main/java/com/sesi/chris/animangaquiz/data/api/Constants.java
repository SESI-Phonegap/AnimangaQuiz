package com.sesi.chris.animangaquiz.data.api;


public class Constants {

    private Constants() {
        throw new IllegalStateException("Constants class");
    }

    //URL Produccion
    public static final String URL_BASE = "https://chrisstek.com";

    public static final class EndPoint{
        private EndPoint() {
            throw new IllegalStateException("EndPoint class");
        }
        public static final String LOGIN_MOBILE = "/AnimangaBackEnd/service/loginmobile.php";
        public static final String GET_ALL_ANIMES = "/AnimangaBackEnd/service/getanimes.php";
        public static final String GET_ALL_ANIMES_IMG = "/AnimangaBackEnd/service/getAnimeImg.php";
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
        public static final String UPDATE_AVATAR = "/AnimangaBackEnd/service/updateAvatar.php";
        public static final String CHECK_USER_FACEBOOK = "/AnimangaBackEnd/service/validaUserFacebook.php";
        public static final String UPDATE_ESFERAS = "/AnimangaBackEnd/service/updateEsferas.php";
        public static final String GET_QUESTIONS_BY_ANIME_IMG = "/AnimangaBackEnd/service/getPreguntasByAnimeImg.php";
        public static final String DELETE_USER = "/AnimangaBackEnd/service/deleteUser.php";

    }

    public static final class ParametersBackEnd{
        private ParametersBackEnd() {
            throw new IllegalStateException("ParametersBackEnd class");
        }
        public static final String USER_NAME = "userName";
        public static final String USER_NAME_FRIEND = "userNameFriend";
        public static final String PASS = "pass";
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
        public static final String AVATAR_BASE64 = "b64Avatar";
        public static final String ESFERAS = "esferas";

    }

    public static final class Deserializer{
        private Deserializer() {
            throw new IllegalStateException("Deserializer class");
        }
        public static final String ANIMES = "animes";
        public static final String PREGUNTAS = "preguntas";
        public static final String WALPAPERS = "wallpapers";
        public static final String FRIENDS = "friends";

    }

    public static final class Compras{
        private Compras() {
            throw new IllegalStateException("Compras class");
        }
        public static final int SMALL_GEMS = 5000;
        public static final int MED_GEMS = 10000;
        public static final int LARGE_GEMS = 20000;
    }
}

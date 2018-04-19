package com.sesi.chris.animangaquiz.data.api;


public class Constants {

    //URL Produccion
    public static final String URL_BASE = "http://www.animangaquiz.mx/";
    // URL WAMP Server localhost Desarrollo
    //public static final String URL_BASE = "http://9.86.173.124:80/";

    // URL MAMP Server localhost Desarrollo
   // public static final String URL_BASE = "http://192.168.0.3:8888/";


    public static final class EndPoint{
        public static final String LOGIN_MOBILE = "/AnimangaBackEnd/service/loginmobile.php";
        public static final String GET_ALL_ANIMES = "/AnimangaBackEnd/service/getanimes.php";
        public static final String GET_QUESTIONS_BY_ANIME_AND_LEVEL = "/AnimangaBackEnd/service/preguntasByAnimeByLeve.php";
        public static final String CHECK_LEVEL_AND_SCORE_BY_ANIME_AND_USER = "/AnimangaBackEnd/service/checkLevelAndScoreByAnimeAndUser.php";
    }

    public static final class ParametersBackEnd{
        public static final String USER_NAME = "userName";
        public static final String PASSWORD = "pass";
        public static final String ID_ANIME = "anime";
        public static final String LEVEL = "level";
        public static final String ID_USER = "iduser";
    }

    public static final class Deserializer{
        public static final String ANIMES = "animes";
        public static final String PREGUNTAS = "preguntas";

    }
}

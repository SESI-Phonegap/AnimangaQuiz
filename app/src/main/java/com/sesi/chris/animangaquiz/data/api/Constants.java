package com.sesi.chris.animangaquiz.data.api;


public class Constants {

    //URL Produccion
    //public static final String URL_BASE = "http://www.animangaquiz.mx/";
    // URL WAMP Server localhost Desarrollo
    public static final String URL_BASE = "http://9.86.173.127:80/";

    // URL MAMP Server localhost Desarrollo
   // public static final String URL_BASE = "http://192.168.0.3:8888/";


    public static final class EndPoint{
        public static final String LOGIN_MOBILE = "/AnimangaBackEnd/service/loginmobile.php";
        public static final String GET_ALL_ANIMES = "/AnimangaBackEnd/service/getanimes.php";
    }

    public static final class ParametersBackEnd{
        public static final String USER_NAME = "userName";
        public static final String PASSWORD = "pass";
    }

    public static final class Deserializer{
        public static final String LOGIN = "login";
        public static final String ANIMES = "animes";
    }
}

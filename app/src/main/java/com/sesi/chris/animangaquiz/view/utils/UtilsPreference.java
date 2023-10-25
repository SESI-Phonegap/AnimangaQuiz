package com.sesi.chris.animangaquiz.view.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.List;

public class UtilsPreference {

    private UtilsPreference(){
        //Empty Constructor
    }

    private static final String SAVE_USER_NAME = "save_user_name";
    private static final String SAVE_EMAIL = "save_email";
    private static final String SAVE_PASS = "save_pass";
    private static final String DEFAULT_VALUE = "";
    private static final String ANIMANGA = "ANIMANGA";

    public static void savePreferenceUserLogin(Context context, String email, String sPass){
        SharedPreferences prefs = context.getSharedPreferences(ANIMANGA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVE_EMAIL,email);
        editor.putString(SAVE_PASS,sPass);
        editor.apply();
    }

    public static void resetPreferenceUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(ANIMANGA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVE_EMAIL, null);
        editor.putString(SAVE_PASS, null);
        editor.apply();
    }

    public static List<String> getUserDataLogin(Context context){
        List<String> lstDataUser = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(ANIMANGA, Context.MODE_PRIVATE);
        lstDataUser.add(prefs.getString(SAVE_EMAIL,DEFAULT_VALUE));
        lstDataUser.add(prefs.getString(SAVE_PASS,DEFAULT_VALUE));
        return lstDataUser;
    }
}

package com.sesi.chris.animangaquiz.view.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class UtilsPreference {

    private static final String SAVE_USER_NAME = "save_user_name";
    private static final String SAVE_PASS = "save_pass";
    private static final String DEFAULT_VALUE = "";

    public static void savePreferenceUserLogin(Context context, String sUser, String sPass){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVE_USER_NAME,sUser);
        editor.putString(SAVE_PASS,sPass);
        editor.apply();
    }

    public static List<String> getUserDataLogin(Context context){
        List<String> lstDataUser = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        lstDataUser.add(prefs.getString(SAVE_USER_NAME,DEFAULT_VALUE));
        lstDataUser.add(prefs.getString(SAVE_PASS,DEFAULT_VALUE));
        return lstDataUser;
    }
}

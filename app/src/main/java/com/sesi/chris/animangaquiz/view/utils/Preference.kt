package com.sesi.chris.animangaquiz.view.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object Preference {

    private const val ANIMANGAQUIZ = "ANIMANGAQUIZ"
    const val USER_DATA = "USER"
    fun setData(prefName: String?, value: Any?, context: Context) {
        val preference:SharedPreferences = context.getSharedPreferences(ANIMANGAQUIZ,Context.MODE_PRIVATE)
        val prefsEditor = preference.edit()
        if (value is Int) {
            prefsEditor.putInt(prefName, (value as Int?)!!)
            prefsEditor.apply()
            return
        }
        if (value is Long) {
            prefsEditor.putLong(prefName, (value as Long?)!!)
            prefsEditor.apply()
            return
        }
        if (value is String) {
            prefsEditor.putString(prefName, value as String?)
            prefsEditor.apply()
            return
        }
        if (value is Boolean) {
            prefsEditor.putBoolean(prefName, (value as Boolean?)!!)
            prefsEditor.apply()
            return
        }

        //Other
        val gson = Gson()
        val json = gson.toJson(value)
        prefsEditor.putString(prefName, json)
        prefsEditor.apply()
    }

    fun <T> getObject(prefName: String?, `object`: Class<T>, context: Context): T {
        val preference:SharedPreferences = context.getSharedPreferences(ANIMANGAQUIZ,Context.MODE_PRIVATE)
        val json = preference.getString(prefName, "")
        val gson = Gson()
        return gson.fromJson(json, `object`)
    }
}
package com.sesi.chris.animangaquiz.view.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.sesi.chris.animangaquiz.data.dto.InternetDto

object InternetUtil {

    fun getConnection(context: Context): InternetDto {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return InternetDto(InternetConnection.MOBILE.connectionName, InternetConnection.MOBILE.isOnline)
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return InternetDto(InternetConnection.WIFI.connectionName, InternetConnection.WIFI.isOnline)
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return InternetDto(InternetConnection.ETHERNET.connectionName, InternetConnection.ETHERNET.isOnline)
            }
        }

        return InternetDto(InternetConnection.NO_INTERNET.connectionName, InternetConnection.NO_INTERNET.isOnline)
    }

}
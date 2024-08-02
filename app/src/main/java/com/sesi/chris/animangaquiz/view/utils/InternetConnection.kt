package com.sesi.chris.animangaquiz.view.utils

enum class InternetConnection(val connectionName: String, val isOnline:Boolean) {
    WIFI("WIFI",true),
    MOBILE("MOBILE",true),
    ETHERNET("ETHERNET",true),
    NO_INTERNET("NO_INTERNET",false)
}
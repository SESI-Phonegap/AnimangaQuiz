package com.sesi.chris.animangaquiz.data.dto

import com.sesi.chris.animangaquiz.view.utils.InternetConnection

data class InternetDto(
    var connectionName: String = InternetConnection.NO_INTERNET.connectionName,
    var isOnline: Boolean = InternetConnection.NO_INTERNET.isOnline
)
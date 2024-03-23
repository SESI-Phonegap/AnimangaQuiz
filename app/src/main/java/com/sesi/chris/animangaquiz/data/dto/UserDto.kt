package com.sesi.chris.animangaquiz.data.dto

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserDto(
    var email: String,
    var userId: Long,
    var pass: String
)
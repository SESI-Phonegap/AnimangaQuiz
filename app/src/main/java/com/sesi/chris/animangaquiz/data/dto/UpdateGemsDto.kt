package com.sesi.chris.animangaquiz.data.dto

data class UpdateGemsDto(
    var email: String,
    var pass: String,
    var idUser: Long,
    var gems: Int = 0
)
package com.jiwondev.trep.model.dto

/** refresh token result도 같음. **/

data class LoginResponse(
    var code: String = "",
//    val refreshToken: String = "",
//    val refreshTokenExpire: String = "",
//    val token: String = "",
//    val tokenExpire: String = "",
    val username: String = "",
    val status: Int = 0,
    val message: String = ""
)
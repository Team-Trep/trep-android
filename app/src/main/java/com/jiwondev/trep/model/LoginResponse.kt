package com.jiwondev.trep.model

/** refresh token result도 같음. **/
data class LoginResponse(
    val refreshToken: String,
    val refreshTokenExpire: String,
    val token: String,
    val tokenExpire: String,
    val username: String
)
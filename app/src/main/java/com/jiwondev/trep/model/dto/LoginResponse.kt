package com.jiwondev.trep.model.dto

/** refresh token result도 같음. **/
data class LoginResponse(
    val refreshToken: String? = null,
    val refreshTokenExpire: String? = null,
    val token: String? = null,
    val tokenExpire: String? = null,
    val username: String? = null
)
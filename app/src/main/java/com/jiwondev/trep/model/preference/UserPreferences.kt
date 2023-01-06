package com.jiwondev.trep.model.preference

data class UserPreferences(
    val autoLogin: Boolean = false,
    val userToken: String = "",
    val userRefreshToken: String = ""
)
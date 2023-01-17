package com.jiwondev.trep.model.dto

data class EmailCodeVerifyResponse(
    val verified: Boolean = false,
    val message: String = "",
    val status: Int = 0
)
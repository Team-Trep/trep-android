package com.jiwondev.trep.model.dto

data class EmailCodeVerifyResponse(
    val verified: Boolean = false,
    val status: Int = 0,
    val code: String? = "0"
)
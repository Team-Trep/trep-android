package com.jiwondev.trep.model.dto

data class SignUpResponse(
    val status: Int = 0,
    val code: String? = "0",
    val id: Int = -1
)
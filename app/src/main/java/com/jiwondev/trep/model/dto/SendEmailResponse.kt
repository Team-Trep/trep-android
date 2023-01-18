package com.jiwondev.trep.model.dto

data class SendEmailResponse(
    val email: String = "",
    val expiredDate: String = "",
    val status: Int = 0,
    val code: String? = "0"
)
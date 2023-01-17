package com.jiwondev.trep.model.dto

data class ExceptionResponse(
    val code: String,
    val message: String,
    val status: Int,
    val timestamp: String
)
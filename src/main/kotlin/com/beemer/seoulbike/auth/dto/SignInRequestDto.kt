package com.beemer.seoulbike.auth.dto

data class SignInRequestDto(
    val email: String,
    val password: String
)

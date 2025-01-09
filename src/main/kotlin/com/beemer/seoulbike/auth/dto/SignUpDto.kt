package com.beemer.seoulbike.auth.dto

data class SignUpDto(
    val nickname: String,
    val email: String,
    val password: String
)

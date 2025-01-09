package com.beemer.seoulbike.auth.dto

import com.beemer.seoulbike.common.dto.TokenDto

data class SignInResponseDto(
    val email: String,
    val nickname: String,
    val token: TokenDto
)

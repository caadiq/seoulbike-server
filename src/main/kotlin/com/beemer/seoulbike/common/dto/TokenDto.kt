package com.beemer.seoulbike.common.dto

data class TokenDto(
    val accessToken: String?,
    val refreshToken: String?
)

package com.beemer.seoulbike.auth.dto

import com.beemer.seoulbike.common.dto.TokenDto
import com.beemer.seoulbike.common.dto.UserInfoDto

data class SignInResponseDto(
    val userInfo: UserInfoDto,
    val token: TokenDto
)

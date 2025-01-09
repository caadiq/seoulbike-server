package com.beemer.seoulbike.common.dto

import java.time.LocalDateTime

data class UserInfoDto(
    val email: String,
    val nickname: String,
    val socialType: String?,
    val createdDate: LocalDateTime?
)

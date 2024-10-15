package com.beemer.seoulbike.common.dto

data class PageDto(
    val previousPage: Int?,
    val currentPage: Int,
    val nextPage: Int?,
)
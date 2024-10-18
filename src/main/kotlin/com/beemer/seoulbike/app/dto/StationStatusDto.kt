package com.beemer.seoulbike.app.dto

data class StationStatusDto(
    val rackCnt: Int?,
    val parkingCnt: Int?,
    val updateTime: String?
)
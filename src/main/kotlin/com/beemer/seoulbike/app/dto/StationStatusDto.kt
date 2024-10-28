package com.beemer.seoulbike.app.dto

data class StationStatusDto(
    val rackCnt: Int?,
    val qrBikeCnt: Int?,
    val elecBikeCnt: Int?,
    val updateTime: String?
)
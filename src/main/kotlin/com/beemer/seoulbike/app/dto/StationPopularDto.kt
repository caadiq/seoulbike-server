package com.beemer.seoulbike.app.dto

data class StationPopularDto(
    val stationId: String,
    val stationNo: String,
    val stationNm: String,
    val rank: Int
)

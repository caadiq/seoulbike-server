package com.beemer.seoulbike.app.dto

data class StationListDto(
    val stationId: String,
    val stationNo: String,
    val stationNm: String,
    val distance: Double?,
    val stationDetails: StationDetailsDto,
    val stationStatus: StationStatusDto,
)


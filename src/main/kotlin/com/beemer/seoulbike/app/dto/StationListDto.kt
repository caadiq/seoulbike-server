package com.beemer.seoulbike.app.dto

data class StationListDto(
    val stationNo: String,
    val stationId: String,
    val stationNm: String,
    val distance: Double?,
    val stationDetails: StationDetailsDto,
    val stationStatus: StationStatusDto,
)


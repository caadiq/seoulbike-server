package com.beemer.seoulbike.app.dto

data class NearbyStationListDto(
    val stationNo: String,
    val stationId: String,
    val stationNm: String,
    val stationDetails: StationDetailsDto,
    val stationStatus: StationStatusDto,
)


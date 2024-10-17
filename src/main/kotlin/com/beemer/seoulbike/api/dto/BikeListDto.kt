package com.beemer.seoulbike.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class BikeListDto(
    @JsonProperty("rentBikeStatus") val rentBikeStatus: RentBikeStatus
)

data class RentBikeStatus(
    @JsonProperty("list_total_count") val listTotalCount: Int,
    @JsonProperty("RESULT") val result: ResultRent,
    @JsonProperty("row") val row: List<RowRent>
)

data class ResultRent(
    @JsonProperty("CODE") val code: String,
    @JsonProperty("MESSAGE") val message: String
)

data class RowRent(
    @JsonProperty("rackTotCnt") val rackTotCnt: String,
    @JsonProperty("stationName") val stationName: String,
    @JsonProperty("parkingBikeTotCnt") val parkingBikeTotCnt: String,
    @JsonProperty("shared") val shared: String,
    @JsonProperty("stationLatitude") val stationLatitude: String,
    @JsonProperty("stationLongitude") val stationLongitude: String,
    @JsonProperty("stationId") val stationId: String
)
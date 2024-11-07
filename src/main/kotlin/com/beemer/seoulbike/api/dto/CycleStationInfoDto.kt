package com.beemer.seoulbike.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CycleStationInfoDto(
    @JsonProperty("stationInfo") val stationInfo: StationInfo
)

data class StationInfo(
    @JsonProperty("list_total_count") val listTotalCount: String,
    @JsonProperty("RESULT") val result: ResultStation,
    @JsonProperty("row") val row: List<RowStation>
)

data class ResultStation(
    @JsonProperty("CODE") val code: String,
    @JsonProperty("MESSAGE") val message: String
)

data class RowStation(
    @JsonProperty("STA_LOC") val staLoc: String,
    @JsonProperty("RENT_ID") val rentId: String,
    @JsonProperty("RENT_NO") val rentNo: String,
    @JsonProperty("RENT_NM") val rentNm: String,
    @JsonProperty("RENT_ID_NM") val rentIdNm: String,
    @JsonProperty("HOLD_NUM") val holdNum: String?,
    @JsonProperty("STA_ADD1") val staAdd1: String,
    @JsonProperty("STA_ADD2") val staAdd2: String,
    @JsonProperty("STA_LAT") val staLat: String,
    @JsonProperty("STA_LONG") val staLong: String,
    @JsonProperty("START_INDEX") val startIndex: Int,
    @JsonProperty("END_INDEX") val endIndex: Int,
    @JsonProperty("RNUM") val rnum: String
)
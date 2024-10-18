package com.beemer.seoulbike.app.dto

import java.time.LocalDateTime

class StationStatusDto(
    val rackCnt: Int?,
    val parkingCnt: Int?,
    val updateTime: String?
)
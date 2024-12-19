package com.beemer.seoulbike.app.dto

import com.beemer.seoulbike.common.dto.CountDto
import com.beemer.seoulbike.common.dto.PageDto

data class StationSearchDto(
    val page: PageDto,
    val count: CountDto,
    val stations: List<StationDto>
)

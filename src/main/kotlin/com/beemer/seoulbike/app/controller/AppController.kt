package com.beemer.seoulbike.app.controller

import com.beemer.seoulbike.app.dto.NearbyStationListDto
import com.beemer.seoulbike.app.service.AppService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/seoulbike")
class AppController(
    private val appService: AppService
) {

    @GetMapping("/stations/nearby")
    fun getNearbyStations(
        @RequestParam("lat") lat: Double,
        @RequestParam("lon") lon: Double,
        @RequestParam("distance") distance: Double
    ) : ResponseEntity<List<NearbyStationListDto>> {
        return appService.getNearbyStations(lat, lon, distance)
    }
}
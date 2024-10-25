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
        @RequestParam("my_lat") myLat: Double,
        @RequestParam("my_lon") myLon: Double,
        @RequestParam("map_lat") mapLat: Double,
        @RequestParam("map_lon") mapLon: Double,
        @RequestParam("distance") distance: Double
    ) : ResponseEntity<List<NearbyStationListDto>> {
        return appService.getNearbyStations(myLat, myLon, mapLat, mapLon, distance)
    }
}
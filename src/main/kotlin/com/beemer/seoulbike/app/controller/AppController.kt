package com.beemer.seoulbike.app.controller

import com.beemer.seoulbike.app.dto.StationListDto
import com.beemer.seoulbike.app.dto.StationSearchDto
import com.beemer.seoulbike.app.service.AppService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    ) : ResponseEntity<List<StationListDto>> {
        return appService.getNearbyStations(myLat, myLon, mapLat, mapLon, distance)
    }

    @GetMapping("/stations")
    fun getStations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") limit: Int,
        @RequestParam("my_lat") myLat: Double,
        @RequestParam("my_lon") myLon: Double,
        @RequestParam query: String
    ) : ResponseEntity<StationSearchDto> {
        return appService.getStations(page, limit, myLat, myLon, query)
    }

    @PostMapping("/stations")
    fun getFavoriteStations(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") limit: Int,
        @RequestParam("my_lat") myLat: Double,
        @RequestParam("my_lon") myLon: Double,
        @RequestBody stationId: List<String>
    ) : ResponseEntity<StationSearchDto> {
        return appService.getFavoriteStations(page, limit, myLat, myLon, stationId)
    }
}
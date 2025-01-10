package com.beemer.seoulbike.app.controller

import com.beemer.seoulbike.app.dto.StationDto
import com.beemer.seoulbike.app.dto.StationPopularDto
import com.beemer.seoulbike.app.dto.StationSearchDto
import com.beemer.seoulbike.app.service.AppService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
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
    ) : ResponseEntity<List<StationDto>> {
        return appService.getNearbyStations(myLat, myLon, mapLat, mapLon, distance)
    }

    @GetMapping("/station")
    fun getStation(
        @RequestParam("my_lat") myLat: Double,
        @RequestParam("my_lon") myLon: Double,
        @RequestParam("station_id") stationId: String
    ) : ResponseEntity<StationDto> {
        return appService.getStation(myLat, myLon, stationId)
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

    @PostMapping("/stations/popular")
    fun setPopularStations(
        @RequestParam stationId: String
    ) : ResponseEntity<Unit> {
        return appService.setPopularStation(stationId)
    }

    @GetMapping("/stations/popular")
    fun getPopularStations() : ResponseEntity<List<StationPopularDto>> {
        return appService.getPopularStations()
    }

    @PostMapping("/stations/favorite/{station_id}")
    fun addFavoriteStation(
        @PathVariable("station_id") stationId: String
    ) : ResponseEntity<Unit> {
        return appService.addFavoriteStation(stationId)
    }
}
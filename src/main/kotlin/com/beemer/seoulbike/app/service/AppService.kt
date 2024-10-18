package com.beemer.seoulbike.app.service

import com.beemer.seoulbike.app.dto.NearbyStationListDto
import com.beemer.seoulbike.app.dto.StationDetailsDto
import com.beemer.seoulbike.app.dto.StationStatusDto
import com.beemer.seoulbike.app.repository.StationsRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class AppService(
    private val stationsRepository: StationsRepository
) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    fun getNearbyStations(lat: Double, lon: Double, distance: Double): ResponseEntity<List<NearbyStationListDto>> {
        val nearbyStations = stationsRepository.findAllByLatAndLonNearby(lat, lon, distance / 100000.0)

        return ResponseEntity.ok(nearbyStations.map {
            NearbyStationListDto(
                stationNo = it.stationNo,
                stationId = it.stationId,
                stationNm = it.stationNm,
                stationDetails = StationDetailsDto(
                    addr1 = it.stationDetails?.stationAddr1,
                    addr2 = it.stationDetails?.stationAddr2,
                    holdNum = it.stationDetails?.holdNum,
                    lat = it.stationDetails?.stationLat,
                    lon = it.stationDetails?.stationLon
                ),
                stationStatus = StationStatusDto(
                    rackCnt = it.liveRentInfo?.rackCnt,
                    parkingCnt = it.liveRentInfo?.parkingCnt,
                    updateTime = it.liveRentInfo?.updateTime?.format(dateTimeFormatter)
                )
            )
        })
    }
}
package com.beemer.seoulbike.app.service

import com.beemer.seoulbike.app.dto.NearbyStationListDto
import com.beemer.seoulbike.app.dto.StationDetailsDto
import com.beemer.seoulbike.app.dto.StationStatusDto
import com.beemer.seoulbike.app.repository.StationsRepository
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class AppService(
    private val stationsRepository: StationsRepository
) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val geometryFactory = GeometryFactory()

    fun getNearbyStations(myLat: Double, myLon: Double, mapLat: Double, mapLon: Double, distance: Double): ResponseEntity<List<NearbyStationListDto>> {
        val nearbyStations = stationsRepository.findAllByLatAndLonNearby(mapLat, mapLon, distance.div(100000.0))

        val currentLocation: Point = geometryFactory.createPoint(org.locationtech.jts.geom.Coordinate(myLon, myLat))

        return ResponseEntity.ok(nearbyStations.map {
            val stationPoint: Point? = it.stationDetails?.geom as? Point
            val distanceToStation: Double? = stationPoint?.distance(currentLocation)?.times(100000.0)

            NearbyStationListDto(
                stationNo = it.stationNo,
                stationId = it.stationId,
                stationNm = it.stationNm,
                distance = distanceToStation,
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
package com.beemer.seoulbike.app.service

import com.beemer.seoulbike.app.dto.StationDetailsDto
import com.beemer.seoulbike.app.dto.StationListDto
import com.beemer.seoulbike.app.dto.StationSearchDto
import com.beemer.seoulbike.app.dto.StationStatusDto
import com.beemer.seoulbike.app.repository.StationsRepository
import com.beemer.seoulbike.common.dto.CountDto
import com.beemer.seoulbike.common.dto.PageDto
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter

@Service
class AppService(
    private val stationsRepository: StationsRepository
) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val geometryFactory = GeometryFactory()

    fun getNearbyStations(myLat: Double, myLon: Double, mapLat: Double, mapLon: Double, distance: Double): ResponseEntity<List<StationListDto>> {
        val nearbyStations = stationsRepository.findAllByLatAndLonNearby(mapLat, mapLon, distance.div(100000.0))

        val currentLocation: Point = geometryFactory.createPoint(org.locationtech.jts.geom.Coordinate(myLon, myLat))

        return ResponseEntity.ok(nearbyStations.map { station ->
            val stationPoint: Point? = station.stationDetails?.geom as? Point
            val distanceToStation: Double? = stationPoint?.distance(currentLocation)?.times(100000.0)

            StationListDto(
                stationId = station.stationId,
                stationNo = station.stationNo,
                stationNm = station.stationNm,
                distance = distanceToStation,
                stationDetails = StationDetailsDto(
                    addr1 = station.stationDetails?.stationAddr1,
                    addr2 = station.stationDetails?.stationAddr2,
                    lat = station.stationDetails?.stationLat,
                    lon = station.stationDetails?.stationLon
                ),
                stationStatus = StationStatusDto(
                    rackCnt = station.stationRealtimeStatus?.rackCnt,
                    qrBikeCnt = station.stationRealtimeStatus?.qrBikeCnt,
                    elecBikeCnt = station.stationRealtimeStatus?.elecBikeCnt,
                    updateTime = station.stationRealtimeStatus?.updateTime?.format(dateTimeFormatter)
                )
            )
        })
    }

    fun getStations(page: Int, limit: Int, myLat: Double, myLon: Double, query: String): ResponseEntity<StationSearchDto> {
        val limitAdjusted = 1.coerceAtLeast(50.coerceAtMost(limit))
        val pageable = PageRequest.of(page, limitAdjusted)

        val stations = stationsRepository.findAllByStationNoOrStationNmOrStationAddr1OrStationAddr2(pageable, myLat, myLon, query)

        val prevPage = if (stations.hasPrevious()) stations.number - 1 else null
        val currentPage = stations.number
        val nextPage = if (stations.hasNext()) stations.number + 1 else null

        val pages = PageDto(prevPage, currentPage, nextPage)

        val totalCount = stations.totalElements
        val currentPageCount = stations.numberOfElements

        val counts = CountDto(totalCount, currentPageCount)

        val currentLocation: Point = geometryFactory.createPoint(org.locationtech.jts.geom.Coordinate(myLon, myLat))

        val stationListDto = stations.content.map { station ->
            val stationPoint: Point? = station.stationDetails?.geom as? Point
            val distanceToStation: Double? = stationPoint?.distance(currentLocation)?.times(100000.0)

            StationListDto(
                stationId = station.stationId,
                stationNo = station.stationNo,
                stationNm = station.stationNm,
                distance = distanceToStation,
                stationDetails = StationDetailsDto(
                    addr1 = station.stationDetails?.stationAddr1,
                    addr2 = station.stationDetails?.stationAddr2,
                    lat = station.stationDetails?.stationLat,
                    lon = station.stationDetails?.stationLon
                ),
                stationStatus = StationStatusDto(
                    rackCnt = station.stationRealtimeStatus?.rackCnt,
                    qrBikeCnt = station.stationRealtimeStatus?.qrBikeCnt,
                    elecBikeCnt = station.stationRealtimeStatus?.elecBikeCnt,
                    updateTime = station.stationRealtimeStatus?.updateTime?.format(dateTimeFormatter)
                )
            )
        }

        return ResponseEntity.ok(StationSearchDto(pages, counts, stationListDto))
    }

    fun getFavoriteStations(page: Int, limit: Int, myLat: Double, myLon: Double, stationId: List<String>): ResponseEntity<StationSearchDto> {
        val limitAdjusted = 1.coerceAtLeast(50.coerceAtMost(limit))
        val pageable = PageRequest.of(page, limitAdjusted)

        val stations = stationsRepository.findFavoriteStationsOrderByDistance(pageable, myLat, myLon, stationId)

        val prevPage = if (stations.hasPrevious()) stations.number - 1 else null
        val currentPage = stations.number
        val nextPage = if (stations.hasNext()) stations.number + 1 else null

        val pages = PageDto(prevPage, currentPage, nextPage)

        val totalCount = stations.totalElements
        val currentPageCount = stations.numberOfElements

        val counts = CountDto(totalCount, currentPageCount)

        val currentLocation: Point = geometryFactory.createPoint(org.locationtech.jts.geom.Coordinate(myLon, myLat))

        val stationListDto = stations.content.map { station ->
            val stationPoint: Point? = station.stationDetails?.geom as? Point
            val distanceToStation: Double? = stationPoint?.distance(currentLocation)?.times(100000.0)

            StationListDto(
                stationId = station.stationId,
                stationNo = station.stationNo,
                stationNm = station.stationNm,
                distance = distanceToStation,
                stationDetails = StationDetailsDto(
                    addr1 = station.stationDetails?.stationAddr1,
                    addr2 = station.stationDetails?.stationAddr2,
                    lat = station.stationDetails?.stationLat,
                    lon = station.stationDetails?.stationLon
                ),
                stationStatus = StationStatusDto(
                    rackCnt = station.stationRealtimeStatus?.rackCnt,
                    qrBikeCnt = station.stationRealtimeStatus?.qrBikeCnt,
                    elecBikeCnt = station.stationRealtimeStatus?.elecBikeCnt,
                    updateTime = station.stationRealtimeStatus?.updateTime?.format(dateTimeFormatter)
                )
            )
        }

        return ResponseEntity.ok(StationSearchDto(pages, counts, stationListDto))
    }
}
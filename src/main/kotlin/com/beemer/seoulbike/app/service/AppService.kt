package com.beemer.seoulbike.app.service

import com.beemer.seoulbike.app.dto.StationPopularDto
import com.beemer.seoulbike.app.dto.StationDetailsDto
import com.beemer.seoulbike.app.dto.StationDto
import com.beemer.seoulbike.app.dto.StationSearchDto
import com.beemer.seoulbike.app.dto.StationStatusDto
import com.beemer.seoulbike.app.entity.FavoriteStations
import com.beemer.seoulbike.app.repository.FavoriteStationsRepository
import com.beemer.seoulbike.app.repository.StationsRepository
import com.beemer.seoulbike.auth.utils.SecurityUtil
import com.beemer.seoulbike.common.dto.CountDto
import com.beemer.seoulbike.common.dto.PageDto
import com.beemer.seoulbike.common.exception.CustomException
import com.beemer.seoulbike.common.exception.ErrorCode
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class AppService(
    private val stationsRepository: StationsRepository,
    private val favoriteStationsRepository: FavoriteStationsRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val securityUtil: SecurityUtil
) {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val geometryFactory = GeometryFactory()

    fun getNearbyStations(myLat: Double, myLon: Double, mapLat: Double, mapLon: Double, distance: Double): ResponseEntity<List<StationDto>> {
        val nearbyStations = stationsRepository.findAllByLatAndLonNearby(mapLat, mapLon, distance.div(100000.0))

        val currentLocation: Point = geometryFactory.createPoint(org.locationtech.jts.geom.Coordinate(myLon, myLat))

        return ResponseEntity.ok(nearbyStations.map { station ->
            val stationPoint: Point? = station.stationDetails?.geom as? Point
            val distanceToStation: Double? = stationPoint?.distance(currentLocation)?.times(100000.0)

            StationDto(
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

    fun getStation( myLat: Double, myLon: Double, stationId: String): ResponseEntity<StationDto> {
        val station = stationsRepository.findByStationId(stationId)

        val currentLocation: Point = geometryFactory.createPoint(org.locationtech.jts.geom.Coordinate(myLon, myLat))
        val stationPoint: Point? = station?.stationDetails?.geom as? Point
        val distanceToStation: Double? = stationPoint?.distance(currentLocation)?.times(100000.0)

        if (station != null) {
            return ResponseEntity.ok(StationDto(
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
            ))
        } else {
            throw CustomException(ErrorCode.STATION_NOT_FOUND)
        }
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

        val stationDto = stations.content.map { station ->
            val stationPoint: Point? = station.stationDetails?.geom as? Point
            val distanceToStation: Double? = stationPoint?.distance(currentLocation)?.times(100000.0)

            StationDto(
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

        return ResponseEntity.ok(StationSearchDto(pages, counts, stationDto))
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

        val stationDto = stations.content.map { station ->
            val stationPoint: Point? = station.stationDetails?.geom as? Point
            val distanceToStation: Double? = stationPoint?.distance(currentLocation)?.times(100000.0)

            StationDto(
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

        return ResponseEntity.ok(StationSearchDto(pages, counts, stationDto))
    }

    fun setPopularStation(stationId: String): ResponseEntity<Unit> {
        redisTemplate.opsForZSet().incrementScore("popularStations", stationId, 1.0)
        return ResponseEntity.ok().build()
    }

    fun getPopularStations(): ResponseEntity<List<StationPopularDto>> {
        val key = "popularStations"
        val zSetOperations = redisTemplate.opsForZSet()
        val typedTuples = zSetOperations.reverseRangeWithScores(key, 0, 9)

        val popularStations = typedTuples?.mapIndexedNotNull { index, typedTuple ->
            typedTuple.value?.let { value ->
                val station = stationsRepository.findByStationId(value)

                station?.let {
                    StationPopularDto(
                        stationId = station.stationId,
                        stationNo = station.stationNo,
                        stationNm = station.stationNm,
                        rank = index + 1
                    )
                }
            }
        } ?: emptyList()

        return ResponseEntity.ok().body(popularStations)
    }

    @Transactional
    fun addFavoriteStation(stationId: String): ResponseEntity<Unit> {
        val userId = securityUtil.getCurrentUser()

        val station = stationsRepository.findByStationId(stationId)
            ?: throw CustomException(ErrorCode.STATION_NOT_FOUND)

        favoriteStationsRepository.findByUserAndStation(userId, station)
            ?.let { throw CustomException(ErrorCode.FAVORITE_STATION_ALREADY_EXISTS) }

        val favoriteStation = FavoriteStations().apply {
            user = userId
            this.station = station
        }

        favoriteStationsRepository.save(favoriteStation)

        return ResponseEntity.ok().build()
    }
}
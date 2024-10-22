package com.beemer.seoulbike.api.service

import com.beemer.seoulbike.api.dto.BikeListDto
import com.beemer.seoulbike.api.dto.CycleStationInfoDto
import com.beemer.seoulbike.app.entity.LiveRentInfo
import com.beemer.seoulbike.app.entity.StationDetails
import com.beemer.seoulbike.app.entity.Stations
import com.beemer.seoulbike.app.repository.StationsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.time.LocalDateTime

@Service
class ApiService(
    private val stationsRepository: StationsRepository,
    private val webClient: WebClient
) {
    @Value("\${data.seoul.api.key}")
    private lateinit var apiKey: String

    private val logger = LoggerFactory.getLogger(ApiService::class.java)

    @Transactional
    fun fetchStations(page: Int) {
        val startPage = page
        var endPage = page + 999

        val url = "http://openapi.seoul.go.kr:8088/$apiKey/json/tbCycleStationInfo/$startPage/$endPage/"

        webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(CycleStationInfoDto::class.java)
            .subscribe({ dto ->
                val totalCount = dto.stationInfo.listTotalCount.toInt()

                if (endPage > totalCount) {
                    endPage = totalCount
                }

                if (dto.stationInfo.row.isNotEmpty()) {
                    val stationsList: MutableList<Stations> = mutableListOf()

                    dto.stationInfo.row.forEach { row ->
                        val station = Stations(
                            stationNo = row.rentNo,
                            stationNm = row.rentNm,
                            stationId = row.rentId,
                        )

                        val stationDetails = StationDetails(
                            stationNo = row.rentNo,
                            holdNum = row.holdNum?.toIntOrNull(),
                            stationAddr1 = row.staAdd1,
                            stationAddr2 = row.staAdd2,
                            stationLat = row.staLat.toDouble(),
                            stationLon = row.staLong.toDouble(),
                            station = station
                        )

                        station.stationDetails = stationDetails
                        stationsList.add(station)
                    }

                    stationsRepository.saveAll(stationsList)

                    if (endPage < totalCount) {
                        fetchStations(endPage + 1)
                    }
                }
            }, { error ->
                logger.error("[오류] fetchStations: ${error.message}")
            })
    }

    @Transactional
    fun fetchLiveRentInfo(page: Int) {
        val startPage = page
        var endPage = page + 999
        var totalCount = 0

        val url = "http://openapi.seoul.go.kr:8088/$apiKey/json/bikeList/$startPage/$endPage/"

        webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(BikeListDto::class.java)
            .subscribe({ dto ->
                totalCount = dto.rentBikeStatus.listTotalCount.toInt()
                val updateTime = LocalDateTime.now()

                if (dto.rentBikeStatus.row.isNotEmpty()) {
                    val stationsList: MutableList<Stations> = mutableListOf()

                    dto.rentBikeStatus.row.forEach { row ->
                        val stations = stationsRepository.findByStationId(row.stationId)
                        stations?.let {
                            val liveRentInfo = LiveRentInfo(
                                stationNo = it.stationNo,
                                stationId = row.stationId,
                                rackCnt = row.rackTotCnt.toIntOrNull(),
                                parkingCnt = row.parkingBikeTotCnt.toIntOrNull(),
                                updateTime = updateTime,
                                station = it
                            )
                            it.liveRentInfo = liveRentInfo
                            stationsList.add(it)
                        }
                    }

                    stationsRepository.saveAll(stationsList)
                    
                    if (totalCount == 1000) {
                        fetchLiveRentInfo(endPage + 1)
                    } else {
                        fetchLiveRentInfo(1)
                    }
                }
            }, { error ->
                error.printStackTrace()
                logger.error("[오류] fetchLiveRentInfo: ${error.message}")

                if (totalCount == 1000) {
                    fetchLiveRentInfo(endPage + 1)
                } else {
                    fetchLiveRentInfo(1)
                }
            })
    }
}
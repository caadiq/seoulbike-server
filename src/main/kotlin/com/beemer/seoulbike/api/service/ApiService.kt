package com.beemer.seoulbike.api.service

import com.beemer.seoulbike.api.dto.CycleStationInfoDto
import com.beemer.seoulbike.api.dto.StationRealtimeStatusDto
import com.beemer.seoulbike.app.entity.StationDetails
import com.beemer.seoulbike.app.entity.StationRealtimeStatus
import com.beemer.seoulbike.app.entity.Stations
import com.beemer.seoulbike.app.repository.StationsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.BodyInserters
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
                            stationId = row.rentId,
                            stationNo = row.rentNo,
                            stationNm = row.rentNm
                        )

                        val stationDetails = StationDetails(
                            stationId = row.rentId,
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
    fun fetchStationRealtimeStatus() {
        val url = "https://www.bikeseoul.com/app/station/getStationRealtimeStatus.do"

        webClient.post()
            .uri(url)
            .body(BodyInserters.fromFormData("stationGrpSeq", "ALL"))
            .retrieve()
            .bodyToMono(StationRealtimeStatusDto::class.java)
            .subscribe({ dto ->
                val updateTime = LocalDateTime.now()

                if (dto.realtimeList.isNotEmpty()) {
                    val stationIds = dto.realtimeList.map { it.stationId }
                    val existingStationsMap = stationsRepository.findByStationIdIn(stationIds)
                        .associateBy { it.stationId }

                    val stationsList: MutableList<Stations> = mutableListOf()

                    dto.realtimeList.forEach { station ->
                        existingStationsMap[station.stationId]?.let { existingStation ->
                            val realtimeStatus = StationRealtimeStatus(
                                stationId = station.stationId,
                                rackCnt = station.rackTotCnt.toIntOrNull(),
                                qrBikeCnt = station.parkingQRBikeCnt.toIntOrNull(),
                                elecBikeCnt = station.parkingELECBikeCnt.toIntOrNull(),
                                updateTime = updateTime,
                                station = existingStation
                            )
                            existingStation.stationRealtimeStatus = realtimeStatus
                            stationsList.add(existingStation)
                        }
                    }

                    stationsRepository.saveAll(stationsList)
                }
            }, { error ->
                error.printStackTrace()
            })
    }
}
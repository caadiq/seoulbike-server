package com.beemer.seoulbike.api.scheduler

import com.beemer.seoulbike.api.service.ApiService
import jakarta.annotation.PostConstruct
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ApiScheduler(private val apiService: ApiService) {

    @PostConstruct
    fun init() {
//        fetchStations()
//        apiService.fetchStationRealtimeStatus()
    }

    @Scheduled(cron = "0 0 * * * *")
    fun fetchStations() {
        apiService.fetchStations(1)
    }

    @Scheduled(fixedRate = 30000)
    fun fetchStationRealtimeStatus() {
        apiService.fetchStationRealtimeStatus()
    }
}
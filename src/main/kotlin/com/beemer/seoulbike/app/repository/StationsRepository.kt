package com.beemer.seoulbike.app.repository

import com.beemer.seoulbike.app.entity.Stations
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StationsRepository : JpaRepository<Stations, String> {
    @Query("SELECT s FROM Stations s WHERE s.stationId = :stationId")
    fun findByStationId(stationId: String): Stations?
}
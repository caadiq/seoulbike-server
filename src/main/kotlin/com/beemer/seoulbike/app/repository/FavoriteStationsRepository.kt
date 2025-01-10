package com.beemer.seoulbike.app.repository

import com.beemer.seoulbike.app.entity.FavoriteStations
import com.beemer.seoulbike.app.entity.Stations
import com.beemer.seoulbike.auth.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface FavoriteStationsRepository : JpaRepository<FavoriteStations, Long> {
    fun findByUserAndStation(user: Users, station: Stations): FavoriteStations?
}
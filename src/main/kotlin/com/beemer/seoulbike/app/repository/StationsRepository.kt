package com.beemer.seoulbike.app.repository

import com.beemer.seoulbike.app.entity.Stations
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StationsRepository : JpaRepository<Stations, String> {
    @Query("SELECT s FROM Stations s WHERE s.stationId = :stationId")
    fun findByStationId(stationId: String): Stations?

    @Query(value = """
        SELECT s.* FROM "Stations" s
        JOIN "StationDetails" sd ON sd.station_no = s.station_no
        WHERE ST_DWithin(
            sd.geom,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
            :distance
        )
    """, nativeQuery = true)
    fun findAllByLatAndLonNearby(@Param("lat") lat: Double, @Param("lon") lon: Double, @Param("distance") distance: Double): List<Stations>
}
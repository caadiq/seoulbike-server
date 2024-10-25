package com.beemer.seoulbike.app.repository

import com.beemer.seoulbike.app.entity.Stations
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface StationsRepository : JpaRepository<Stations, String> {
    @Query("SELECT s FROM Stations s WHERE s.stationId = :stationId")
    fun findByStationId(stationId: String): Stations?

    @Query("""
        SELECT s.* FROM "Stations" s
        JOIN "StationDetails" sd ON sd.station_no = s.station_no
        WHERE ST_DWithin(
            sd.geom,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
            :distance
        )
    """, nativeQuery = true)
    fun findAllByLatAndLonNearby(@Param("lat") lat: Double, @Param("lon") lon: Double, @Param("distance") distance: Double): List<Stations>

    @Query("""
        SELECT s FROM Stations s
        LEFT JOIN s.stationDetails sd
        WHERE s.stationNo LIKE %:query%
        OR s.stationNm LIKE %:query%
        OR sd.stationAddr1 LIKE %:query%
        OR sd.stationAddr2 LIKE %:query%
    """)
    fun findAllByStationNoOrStationNmOrStationAddr1OrStationAddr2(pageable: Pageable, query: String): Page<Stations>
}
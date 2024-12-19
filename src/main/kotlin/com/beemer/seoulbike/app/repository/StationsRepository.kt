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

    fun findByStationId(stationId: String): Stations?

    @Query("SELECT s FROM Stations s WHERE s.stationId IN :stationIds")
    fun findByStationIdIn(@Param("stationIds") stationIds: List<String>): List<Stations>

    @Query("""
        SELECT s.* FROM "Stations" s
        JOIN "StationDetails" sd ON sd.station_id = s.station_id
        WHERE ST_DWithin(
            sd.geom,
            ST_SetSRID(ST_MakePoint(:lon, :lat), 4326),
            :distance
        )
    """, nativeQuery = true)
    fun findAllByLatAndLonNearby(@Param("lat") lat: Double, @Param("lon") lon: Double, @Param("distance") distance: Double): List<Stations>

    @Query("""
        SELECT s.*, ST_Distance(sd.geom, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)) AS distance 
        FROM "Stations" s
        LEFT JOIN "StationDetails" sd ON sd.station_id = s.station_id
        WHERE s.station_no LIKE %:query%
        OR s.station_nm LIKE %:query%
        OR sd.station_addr1 LIKE %:query%
        OR sd.station_addr2 LIKE %:query%
        ORDER BY distance ASC
    """, nativeQuery = true)
    fun findAllByStationNoOrStationNmOrStationAddr1OrStationAddr2(pageable: Pageable, @Param("lat") lat: Double, @Param("lon") lon: Double, @Param("query") query: String): Page<Stations>


    @Query("""
        SELECT s.*, ST_Distance(sd.geom, ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)) AS distance 
        FROM "Stations" s
        JOIN "StationDetails" sd ON sd.station_id = s.station_id
        WHERE s.station_id IN :stationIds
        ORDER BY distance ASC
    """, nativeQuery = true)
    fun findFavoriteStationsOrderByDistance(pageable: Pageable, @Param("lat") lat: Double, @Param("lon") lon: Double, @Param("stationIds") stationIds: List<String>, ): Page<Stations>
}
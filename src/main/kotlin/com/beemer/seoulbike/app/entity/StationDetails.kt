package com.beemer.seoulbike.app.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.locationtech.jts.geom.Geometry

@Entity
@Table(name = "\"StationDetails\"")
data class StationDetails(
    @Id
    @Column(name = "station_id", nullable = false)
    val stationId: String,

    @Column(name = "station_addr1")
    val stationAddr1: String?,

    @Column(name = "station_addr2")
    val stationAddr2: String?,

    @Column(name = "station_lat")
    val stationLat: Double?,

    @Column(name = "station_lon")
    val stationLon: Double?,

    @Column(name = "geom", columnDefinition = "geometry(Point,4326)")
    val geom: Geometry? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @MapsId
    val station: Stations
)

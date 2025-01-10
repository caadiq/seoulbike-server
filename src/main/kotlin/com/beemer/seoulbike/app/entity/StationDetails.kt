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
class StationDetails(
    stationId: String,
    stationAddr1: String?,
    stationAddr2: String?,
    stationLat: Double?,
    stationLon: Double?
) {
    @Id
    @Column(name = "station_id", nullable = false)
    var stationId: String = stationId
        protected set

    @Column(name = "station_addr1")
    var stationAddr1: String? = stationAddr1
        protected set

    @Column(name = "station_addr2")
    var stationAddr2: String? = stationAddr2
        protected set

    @Column(name = "station_lat")
    var stationLat: Double? = stationLat
        protected set

    @Column(name = "station_lon")
    var stationLon: Double? = stationLon
        protected set

    @Column(name = "geom", columnDefinition = "geometry(Point,4326)")
    var geom: Geometry? = null
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @MapsId
    var station: Stations? = null
}

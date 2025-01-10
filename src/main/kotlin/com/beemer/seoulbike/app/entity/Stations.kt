package com.beemer.seoulbike.app.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "\"Stations\"")
class Stations(
    stationId: String,
    stationNo: String,
    stationNm: String
) {
    @Id
    @Column(name = "station_id", nullable = false)
    var stationId: String = stationId
        protected set

    @Column(name = "station_no", nullable = false)
    var stationNo: String = stationNo
        protected set

    @Column(name = "station_nm", nullable = false)
    var stationNm: String = stationNm
        protected set

    @OneToOne(mappedBy = "station", cascade = [CascadeType.ALL])
    var stationDetails: StationDetails? = null

    @OneToOne(mappedBy = "station", cascade = [CascadeType.ALL])
    var stationRealtimeStatus: StationRealtimeStatus? = null

    @OneToMany(mappedBy = "station", cascade = [CascadeType.ALL])
    var favoriteStations: MutableList<FavoriteStations> = mutableListOf()
}
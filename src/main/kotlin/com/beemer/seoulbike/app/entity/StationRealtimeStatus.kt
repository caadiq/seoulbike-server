package com.beemer.seoulbike.app.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "\"StationRealtimeStatus\"")
data class StationRealtimeStatus(
    @Id
    @Column(name = "station_id", nullable = false)
    val stationId: String,

    @Column(name = "rack_cnt")
    val rackCnt: Int?,

    @Column(name = "qr_bike_cnt")
    val qrBikeCnt: Int?,

    @Column(name = "elec_bike_cnt")
    val elecBikeCnt: Int?,

    @Column(name = "update_time", nullable = false)
    val updateTime: LocalDateTime,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @MapsId
    val station: Stations
)

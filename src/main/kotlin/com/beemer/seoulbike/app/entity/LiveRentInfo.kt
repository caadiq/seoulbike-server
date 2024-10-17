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
@Table(name = "LiveRentInfo")
data class LiveRentInfo(
    @Id
    @Column(name = "station_no", nullable = false)
    val stationNo: String,

    @Column(name = "station_id", nullable = false)
    val stationId: String,

    @Column(name = "rack_cnt")
    val rackCnt: Int?,

    @Column(name = "parking_cnt")
    val parkingCnt: Int?,

    @Column(name = "update_time", nullable = false)
    val updateTime: LocalDateTime,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_no")
    @MapsId
    val station: Stations
)

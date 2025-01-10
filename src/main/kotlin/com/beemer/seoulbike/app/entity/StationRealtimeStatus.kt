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
class StationRealtimeStatus(
    stationId: String,
    rackCnt: Int?,
    qrBikeCnt: Int?,
    elecBikeCnt: Int?,
    updateTime: LocalDateTime
) {
    @Id
    @Column(name = "station_id", nullable = false)
    var stationId: String = stationId
        protected set

    @Column(name = "rack_cnt")
    var rackCnt: Int? = rackCnt
        protected set

    @Column(name = "qr_bike_cnt")
    var qrBikeCnt: Int? = qrBikeCnt
        protected set

    @Column(name = "elec_bike_cnt")
    var elecBikeCnt: Int? = elecBikeCnt
        protected set

    @Column(name = "update_time", nullable = false)
    var updateTime: LocalDateTime = updateTime
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @MapsId
    var station: Stations? = null
}

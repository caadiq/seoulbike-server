package com.beemer.seoulbike.app.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "Stations")
data class Stations(
    @Id
    @Column(name = "station_no", nullable = false)
    val stationNo: String,

    @Column(name = "station_id")
    val stationId: String?,

    @Column(name = "station_nm")
    val stationNm: String?,

    @OneToOne(mappedBy = "station", cascade = [CascadeType.ALL])
    var stationDetails: StationDetails? = null
)


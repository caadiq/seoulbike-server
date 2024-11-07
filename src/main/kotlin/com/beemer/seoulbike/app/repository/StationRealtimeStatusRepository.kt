package com.beemer.seoulbike.app.repository

import com.beemer.seoulbike.app.entity.StationRealtimeStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StationRealtimeStatusRepository : JpaRepository<StationRealtimeStatus, String>
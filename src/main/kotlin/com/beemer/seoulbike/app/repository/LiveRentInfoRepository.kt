package com.beemer.seoulbike.app.repository

import com.beemer.seoulbike.app.entity.LiveRentInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LiveRentInfoRepository : JpaRepository<LiveRentInfo, String>
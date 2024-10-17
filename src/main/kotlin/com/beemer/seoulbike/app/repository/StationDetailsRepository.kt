package com.beemer.seoulbike.app.repository

import com.beemer.seoulbike.app.entity.StationDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StationDetailsRepository : JpaRepository<StationDetails, String>
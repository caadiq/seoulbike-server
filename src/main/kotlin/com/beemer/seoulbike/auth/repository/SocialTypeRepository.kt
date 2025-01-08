package com.beemer.seoulbike.auth.repository

import com.beemer.seoulbike.auth.entity.SocialType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialTypeRepository : JpaRepository<SocialType, String>
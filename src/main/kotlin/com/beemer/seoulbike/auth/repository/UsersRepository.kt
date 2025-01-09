package com.beemer.seoulbike.auth.repository

import com.beemer.seoulbike.auth.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UsersRepository : JpaRepository<Users, UUID> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<Users>
}
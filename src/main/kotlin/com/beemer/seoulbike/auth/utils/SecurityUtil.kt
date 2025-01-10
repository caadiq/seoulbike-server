package com.beemer.seoulbike.auth.utils

import com.beemer.seoulbike.auth.entity.Users
import com.beemer.seoulbike.auth.repository.UsersRepository
import com.beemer.seoulbike.common.exception.CustomException
import com.beemer.seoulbike.common.exception.ErrorCode
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class SecurityUtil(
    private val usersRepository: UsersRepository
) {
    fun getCurrentUser(): Users {
        val user = getCurrentUsername().flatMap(usersRepository::findByEmail)

        if (user.isEmpty)
            throw CustomException(ErrorCode.UNAUTHORIZED)

        return user.get()
    }

    private fun getCurrentUsername(): Optional<String> {
        val authentication = SecurityContextHolder.getContext().authentication ?: return Optional.empty()

        var username: String? = null
        if (authentication.principal is UserDetails) {
            val springSecurityUser = authentication.principal as UserDetails
            username = springSecurityUser.username
        } else if (authentication.principal is String) {
            username = authentication.principal as String
        }

        return Optional.ofNullable(username)
    }
}
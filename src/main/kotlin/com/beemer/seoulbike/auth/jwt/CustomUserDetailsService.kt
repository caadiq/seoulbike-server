package com.beemer.seoulbike.auth.jwt

import com.beemer.seoulbike.auth.entity.Users
import com.beemer.seoulbike.auth.repository.UsersRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val usersRepository: UsersRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return usersRepository.findByEmail(username)
            .map(::createUser)
            .orElseThrow { throw UsernameNotFoundException("사용자를 찾을 수 없습니다.") }

    }

    private fun createUser(user: Users): User {
        return User(user.email, user.password, emptyList())
    }
}
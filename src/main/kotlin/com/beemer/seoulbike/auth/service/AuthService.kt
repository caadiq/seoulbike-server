package com.beemer.seoulbike.auth.service

import com.beemer.seoulbike.auth.dto.SignUpDto
import com.beemer.seoulbike.auth.entity.Users
import com.beemer.seoulbike.auth.repository.SocialTypeRepository
import com.beemer.seoulbike.auth.repository.UsersRepository
import com.beemer.seoulbike.common.exception.CustomException
import com.beemer.seoulbike.common.exception.ErrorCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val usersRepository: UsersRepository,
    private val socialTypeRepository: SocialTypeRepository
) {
    @Transactional
    fun signUp(dto: SignUpDto) : ResponseEntity<Unit> {
        if (usersRepository.existsByEmail(dto.email))
            throw CustomException(ErrorCode.EMAIL_DUPLICATION)

        val socialType = socialTypeRepository.findById("email").orElseThrow {
            CustomException(ErrorCode.SOCIAL_TYPE_NOT_FOUND)
        }

        val users = Users(
            nickname = dto.nickname,
            email = dto.email,
            password = dto.password,
            socialType = socialType
        )

        usersRepository.save(users)

        return ResponseEntity.ok().build()
    }
}
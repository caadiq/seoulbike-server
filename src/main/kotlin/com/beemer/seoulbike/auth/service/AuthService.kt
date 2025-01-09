package com.beemer.seoulbike.auth.service

import com.beemer.seoulbike.auth.dto.SignUpDto
import com.beemer.seoulbike.auth.entity.Users
import com.beemer.seoulbike.auth.repository.SocialTypeRepository
import com.beemer.seoulbike.auth.repository.UsersRepository
import com.beemer.seoulbike.common.exception.CustomException
import com.beemer.seoulbike.common.exception.ErrorCode
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val usersRepository: UsersRepository,
    private val socialTypeRepository: SocialTypeRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @Transactional
    fun signUp(dto: SignUpDto) : ResponseEntity<Unit> {
        val nickname = dto.nickname.trim()
        val email = dto.email.trim()
        val password = dto.password.trim()

        if (nickname.isEmpty())
            throw CustomException(ErrorCode.NICKNAME_EMPTY)

        if (!nickname.matches(Regex("^[가-힣a-zA-Z]{2,16}\$")))
            throw CustomException(ErrorCode.NICKNAME_INVALID)

        if (email.isEmpty())
            throw CustomException(ErrorCode.EMAIL_EMPTY)

        if (!email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")))
            throw CustomException(ErrorCode.EMAIL_INVALID)

        if (password.isEmpty())
            throw CustomException(ErrorCode.PASSWORD_EMPTY)

        if (!password.matches(Regex("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,16}\$")))
            throw CustomException(ErrorCode.PASSWORD_INVALID)

        if (usersRepository.existsByEmail(dto.email))
            throw CustomException(ErrorCode.EMAIL_DUPLICATION)

        val socialType = socialTypeRepository.findById("email").orElseThrow {
            CustomException(ErrorCode.SOCIAL_TYPE_NOT_FOUND)
        }

        val users = Users(
            nickname = dto.nickname,
            email = dto.email,
            password = bCryptPasswordEncoder.encode(dto.password)
        )

        users.socialType = socialType

        usersRepository.save(users)

        return ResponseEntity.ok().build()
    }
}
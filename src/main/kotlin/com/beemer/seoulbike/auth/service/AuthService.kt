package com.beemer.seoulbike.auth.service

import com.beemer.seoulbike.auth.dto.SignInRequestDto
import com.beemer.seoulbike.auth.dto.SignInResponseDto
import com.beemer.seoulbike.auth.dto.SignUpDto
import com.beemer.seoulbike.common.dto.UserInfoDto
import com.beemer.seoulbike.auth.entity.Users
import com.beemer.seoulbike.auth.jwt.JwtTokenProvider
import com.beemer.seoulbike.auth.repository.SocialTypeRepository
import com.beemer.seoulbike.auth.repository.UsersRepository
import com.beemer.seoulbike.auth.utils.SecurityUtil
import com.beemer.seoulbike.common.dto.TokenDto
import com.beemer.seoulbike.common.exception.CustomException
import com.beemer.seoulbike.common.exception.ErrorCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val usersRepository: UsersRepository,
    private val socialTypeRepository: SocialTypeRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val securityUtil: SecurityUtil
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

    fun signIn(dto: SignInRequestDto): ResponseEntity<SignInResponseDto> {
        val email = dto.email.trim()
        val password = dto.password.trim()

        if (email.isEmpty())
            throw CustomException(ErrorCode.EMAIL_EMPTY)

        if (password.isEmpty())
            throw CustomException(ErrorCode.PASSWORD_EMPTY)

        try {
            val authenticationToken = UsernamePasswordAuthenticationToken(email, password)

            val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

            val accessToken = jwtTokenProvider.generateAccessToken(authentication)
            val refreshToken = jwtTokenProvider.generateRefreshToken()

            val user = usersRepository.findByEmail(authentication.name)
                .orElseThrow { throw CustomException(ErrorCode.USER_NOT_FOUND) }

            return ResponseEntity.ok(
                SignInResponseDto(
                    userInfo = UserInfoDto(
                        email = user.email,
                        nickname = user.nickname,
                        socialType = user.socialType?.socialType,
                        createdDate = user.createdDate
                    ),
                    token = TokenDto(
                        accessToken = accessToken,
                        refreshToken = refreshToken
                    )
                )
            )
        } catch (e: BadCredentialsException) {
            throw CustomException(ErrorCode.BAD_CREDENTIALS)
        }
    }

    fun getUserInfo(): ResponseEntity<UserInfoDto> {
        val user = securityUtil.getCurrentUser()

        return ResponseEntity.ok(
            UserInfoDto(
                email = user.email,
                nickname = user.nickname,
                socialType = user.socialType?.socialType,
                createdDate = user.createdDate
            )
        )
    }
}
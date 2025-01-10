package com.beemer.seoulbike.auth.controller

import com.beemer.seoulbike.auth.dto.SignInRequestDto
import com.beemer.seoulbike.auth.dto.SignInResponseDto
import com.beemer.seoulbike.auth.dto.SignUpDto
import com.beemer.seoulbike.auth.service.AuthService
import com.beemer.seoulbike.common.dto.UserInfoDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signUp(@RequestBody dto: SignUpDto) : ResponseEntity<Unit> {
        return authService.signUp(dto)
    }

    @PostMapping("/signin")
    fun signIn(@RequestBody dto: SignInRequestDto) : ResponseEntity<SignInResponseDto> {
        return authService.signIn(dto)
    }

    @GetMapping("/user")
    fun getUser() : ResponseEntity<UserInfoDto> {
        return authService.getUserInfo()
    }
}
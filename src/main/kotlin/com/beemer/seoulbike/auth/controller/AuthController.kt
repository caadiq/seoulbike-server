package com.beemer.seoulbike.auth.controller

import com.beemer.seoulbike.auth.dto.SignUpDto
import com.beemer.seoulbike.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody dto: SignUpDto) : ResponseEntity<Unit> {
        return authService.signUp(dto)
    }
}
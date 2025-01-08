package com.beemer.seoulbike.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignUpDto(
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,16}$", message = "닉네임은 한글, 영문을 포함한 2~16자리여야 합니다.")
    val nickname: String,

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "잘못된 이메일 형식입니다.")
    val email: String,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()-_?]).{8,16}$", message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~16자리여야 합니다.")
    val password: String
)

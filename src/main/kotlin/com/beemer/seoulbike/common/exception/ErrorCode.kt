package com.beemer.seoulbike.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val message: String?
) {
    /**
     * 400 Bad Request
     * 클라이언트가 잘못된 요청을 보냈을 때 발생하는 에러 코드
     * ex) 필수 요청 파라미터가 누락된 경우
     */
    INVALID_FIELD(HttpStatus.BAD_REQUEST, "잘못된 필드입니다."),
    NICKNAME_EMPTY(HttpStatus.BAD_REQUEST, "닉네임을 입력해주세요."),
    NICKNAME_INVALID(HttpStatus.BAD_REQUEST, "닉네임은 한글, 영문을 포함한 2~16자리여야 합니다."),
    EMAIL_EMPTY(HttpStatus.BAD_REQUEST, "이메일을 입력해주세요."),
    EMAIL_INVALID(HttpStatus.BAD_REQUEST, "잘못된 이메일 형식입니다."),
    PASSWORD_EMPTY(HttpStatus.BAD_REQUEST, "비밀번호를 입력해주세요."),
    PASSWORD_INVALID(HttpStatus.BAD_REQUEST, "비밀번호는 영문, 숫자, 특수문자를 포함한 8~16자리여야 합니다."),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다."),


    /**
     * 401 Unauthorized
     * 클라이언트가 인증되지 않은 상태에서 보호된 리소스에 접근하려고 할 때 발생하는 에러 코드
     * ex) 로그인하지 않은 사용자가 로그인이 필요한 페이지에 접근할 때
     */


    /**
     * 403 Forbidden
     * 권한이 없는 사용자가 리소스에 접근하려고 할 때 발생하는 에러 코드
     * ex) 일반 사용자가 관리자 페이지에 접근할 때
     */


    /**
     * 404 Not Found
     * 클라이언트가 요청한 리소스를 찾을 수 없을 때 발생하는 에러 코드
     * ex) 존재하지 않는 데이터를 조회할 때
     */
    STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 대여소를 찾을 수 없습니다."),
    SOCIAL_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 소셜 타입을 찾을 수 없습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),


    /**
     * 409 Conflict
     * 클라이언트의 요청이 서버의 상태와 충돌이 발생했을 때 발생하는 에러 코드
     * ex) 이미 존재하는 데이터를 생성하려고 할 때
     */
    EMAIL_DUPLICATION(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    /**
     * 500 Internal Server Error
     * 서버에 오류가 발생했을 때 발생하는 에러 코드
     * ex) 서버에서 처리되지 않은 예외가 발생했을 때
     */
}
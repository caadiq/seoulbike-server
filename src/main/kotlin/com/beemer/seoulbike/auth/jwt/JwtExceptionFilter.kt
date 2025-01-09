package com.beemer.seoulbike.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.LocalDateTime

@Component
class JwtExceptionFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: JwtTokenException) {
            response.sendJwtError(HttpStatus.UNAUTHORIZED, e)
        }
    }

    private fun HttpServletResponse.sendJwtError(status: HttpStatus, e: Throwable) {
        this.status = status.value()
        this.contentType = "application/json; charset=UTF-8"
        this.writer.write(
            JwtErrorResponse(
                timestamp = LocalDateTime.now().toString(),
                status = status.value(),
                error = status.reasonPhrase,
                message = e.message ?: "Unknown error"
            ).convertToJson()
        )
    }
}
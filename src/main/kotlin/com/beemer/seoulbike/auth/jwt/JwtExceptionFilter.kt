package com.beemer.seoulbike.auth.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.time.LocalDateTime

@Component
class JwtExceptionFilter : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: JwtTokenException) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e)
        }
    }

    @Throws(IOException::class)
    fun setErrorResponse(status: HttpStatus, response: HttpServletResponse, e: Throwable) {
        response.status = status.value()
        response.contentType = "application/json; charset=UTF-8"
        response.writer.write(
            JwtErrorResponse(
                timestamp = LocalDateTime.now().toString(),
                status = status.value(),
                error = status.reasonPhrase,
                message = e.message ?: "Unknown error"
            ).convertToJson()
        )
    }
}
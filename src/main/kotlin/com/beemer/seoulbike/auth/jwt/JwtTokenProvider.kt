package com.beemer.seoulbike.auth.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider {
    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${jwt.token.access-expiration-time}")
    private var accessTokenExpirationTime: Long = 0

    @Value("\${jwt.token.refresh-expiration-time}")
    private var refreshTokenExpirationTime: Long = 0

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateAccessToken(authentication: Authentication): String {
        return Jwts.builder()
            .subject(authentication.name)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + accessTokenExpirationTime))
            .signWith(getSigningKey())
            .compact()
    }

    fun generateRefreshToken(): String {
        return Jwts.builder()
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + refreshTokenExpirationTime))
            .signWith(getSigningKey())
            .compact()
    }

    fun reissueAccessToken(subject: String): String {
        return Jwts.builder()
            .subject(subject)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + accessTokenExpirationTime))
            .signWith(getSigningKey())
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = extractAllClaims(token)

        val principal: UserDetails = User(claims.subject, "", emptyList())
        return UsernamePasswordAuthenticationToken(principal, token, principal.authorities)
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: SignatureException) {
            throw JwtException("JWT 토큰의 서명이 올바르지 않습니다.")
        } catch (e: SecurityException) {
            throw JwtException("JWT 토큰의 보안 예외가 발생했습니다.")
        } catch (e: MalformedJwtException) {
            throw JwtException("JWT 토큰이 잘못되었습니다.")
        } catch (e: ExpiredJwtException) {
            throw JwtException("JWT 토큰이 만료되었습니다.")
        } catch (e: UnsupportedJwtException) {
            throw JwtException("지원하지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            throw JwtException("JWT 토큰이 비어있습니다.")
        }
    }

    fun extractAllClaims(token: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }
}
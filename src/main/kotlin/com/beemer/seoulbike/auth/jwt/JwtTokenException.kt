package com.beemer.seoulbike.auth.jwt

import io.jsonwebtoken.JwtException

class JwtTokenException(message: String) : JwtException(message)
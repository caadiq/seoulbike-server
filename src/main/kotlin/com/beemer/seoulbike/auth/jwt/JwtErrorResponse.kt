package com.beemer.seoulbike.auth.jwt

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

private val objectMapper = ObjectMapper()

data class JwtErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String
) {
    @Throws(JsonProcessingException::class)
    fun convertToJson(): String = objectMapper.writeValueAsString(this)
}
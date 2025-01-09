package com.beemer.seoulbike.auth.jwt

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

data class JwtErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String
) {
    companion object {
        private val objectMapper = ObjectMapper()
    }

    @Throws(JsonProcessingException::class)
    fun convertToJson(): String {
        return objectMapper.writeValueAsString(this)
    }
}
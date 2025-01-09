package com.beemer.seoulbike.auth.config

import com.beemer.seoulbike.auth.jwt.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtExceptionFilter: JwtExceptionFilter
) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .httpBasic(AbstractHttpConfigurer<*, *>::disable)

            .csrf(AbstractHttpConfigurer<*, *>::disable)

            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            .exceptionHandling { exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
            }

            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers(HttpMethod.POST, "/signup").permitAll()
                    .requestMatchers(HttpMethod.POST, "/signin").permitAll()

                    .requestMatchers(HttpMethod.GET, "/station").permitAll()
                    .requestMatchers(HttpMethod.GET, "/stations").permitAll()
                    .requestMatchers(HttpMethod.POST, "/stations").permitAll()
                    .requestMatchers(HttpMethod.GET, "/stations/nearby").permitAll()
                    .requestMatchers(HttpMethod.GET, "/stations/popular").permitAll()
                    .requestMatchers(HttpMethod.POST, "/stations/popular").permitAll()

                    .anyRequest().authenticated()
            }

            .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter::class.java)

        return httpSecurity.build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
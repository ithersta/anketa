package com.ithersta.anketa.auth.filter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/oauth/**", permitAll)
                authorize("/survey/**", permitAll)
                authorize("/**", authenticated)
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            csrf { disable() }
            cors { disable() }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthFilter)
        }
        return http.build()
    }
}
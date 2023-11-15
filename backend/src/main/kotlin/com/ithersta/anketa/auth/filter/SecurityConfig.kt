package com.ithersta.anketa.auth.filter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            authorizeExchange {
                authorize("/oauth/**", permitAll)
                authorize("/survey/**", permitAll)
                authorize("/**", authenticated)
            }
            csrf { disable() }
            cors { disable() }
        }
    }
}
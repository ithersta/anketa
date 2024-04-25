package com.ithersta.anketa.auth.filter

import com.ithersta.anketa.auth.services.JwtService
import com.ithersta.anketa.auth.services.WhitelistService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.invoke
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import reactor.core.publisher.Mono

@Configuration
class SecurityConfig(
    private val jwtService: JwtService,
    private val authManager: AuthManager,
    private val whitelistService: WhitelistService,
) {
    @Bean
    fun filterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            authorizeExchange {
                authorize("/oauth/**", permitAll)
                authorize("/survey/**", permitAll)
                authorize("/answer/**", permitAll)
                authorize("/**", authenticated)
            }
            csrf { disable() }
            cors { disable() }
            formLogin { disable() }
            httpBasic { disable() }
            addFilterAt(bearerFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
        }
    }

    private fun bearerFilter() =
        AuthenticationWebFilter(authManager).apply {
            setServerAuthenticationConverter { exchange ->
                val header = exchange.request.headers["Authorization"]
                    ?.firstOrNull { it.startsWith("Bearer ") } ?: return@setServerAuthenticationConverter Mono.empty()
                val token = header.drop(7)
                val authentication = runCatching {
                    val (userId, email) = jwtService.getIdAndEmailFromToken(token)
                    if (whitelistService.isAllowed(email)) {
                        UsernamePasswordAuthenticationToken(userId, null, emptyList())
                    } else {
                        null
                    }
                }.getOrNull()
                Mono.justOrEmpty(authentication)
            }
        }
}

package com.ithersta.anketa.auth.filter

import com.ithersta.anketa.auth.services.JwtService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {
        val header = exchange.request.headers["Authorization"]
            ?.firstOrNull { it.startsWith("Bearer ") }
        if (header != null) {
            val token = header.drop(7)
            runCatching {
                jwtService.getUserIdFromToken(token)
            }.onSuccess { userId ->
                SecurityContextHolder.getContext().authentication = JwtAuthenticationToken(userId)
            }
        }
        return chain.filter(exchange)
    }
}
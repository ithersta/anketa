package com.ithersta.anketa.auth.filter

import com.ithersta.anketa.auth.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.drop(7)
            runCatching {
                jwtService.getUserIdFromToken(token)
            }.onSuccess { userId ->
                SecurityContextHolder.getContext().authentication = JwtAuthenticationToken(userId)
            }
        }
        filterChain.doFilter(request, response)
    }
}
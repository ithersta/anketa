package com.ithersta.anketa.auth.filter

import org.springframework.security.authentication.AbstractAuthenticationToken
import java.util.UUID

class JwtAuthenticationToken(
    private val userId: UUID,
) : AbstractAuthenticationToken(emptyList()) {
    init { isAuthenticated = true }
    override fun getCredentials(): Nothing? = null
    override fun getPrincipal(): UUID = userId
}
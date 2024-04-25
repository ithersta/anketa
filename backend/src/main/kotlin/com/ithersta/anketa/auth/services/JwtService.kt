package com.ithersta.anketa.auth.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

private const val ID_CLAIM = "id"
private const val EMAIL_CLAIM = "email"

@Service
class JwtService(
    secretService: SecretService,
) {
    private val algorithm = Algorithm.HMAC256(secretService.secrets.jwtSecret)
    private val durationUntilExpiration = Duration.ofDays(365)
    private val verifier = JWT.require(algorithm)
        .build()

    fun generateToken(userId: UUID, email: String): String = JWT.create()
        .withClaim(ID_CLAIM, userId.toString())
        .withClaim(EMAIL_CLAIM, email)
        .withExpiresAt(Instant.now() + durationUntilExpiration)
        .sign(algorithm)

    fun getIdAndEmailFromToken(token: String): Pair<UUID, String> {
        val verifiedToken = verifier.verify(token)
        val idClaim = verifiedToken.getClaim(ID_CLAIM)
        val emailClaim = verifiedToken.getClaim(EMAIL_CLAIM)
        return UUID.fromString(idClaim.asString()) to emailClaim.asString()
    }
}

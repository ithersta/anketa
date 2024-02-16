package com.ithersta.anketa.auth.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

private const val ID_CLAIM = "id"

@Service
class JwtService(
    secretService: SecretService,
) {
    private val algorithm = Algorithm.HMAC256(secretService.secrets.jwtSecret)
    private val durationUntilExpiration = Duration.ofDays(365)
    private val verifier = JWT.require(algorithm)
        .build()

    fun generateToken(userId: UUID): String = JWT.create()
        .withClaim(ID_CLAIM, userId.toString())
        .withExpiresAt(Instant.now() + durationUntilExpiration)
        .sign(algorithm)

    fun getUserIdFromToken(token: String): UUID {
        val idClaim = verifier.verify(token)
            .getClaim(ID_CLAIM)
        return UUID.fromString(idClaim.asString())
    }
}

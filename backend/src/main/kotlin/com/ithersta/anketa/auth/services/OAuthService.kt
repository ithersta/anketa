package com.ithersta.anketa.auth.services

import com.ithersta.anketa.auth.OAuthProvider
import com.ithersta.anketa.auth.data.repositories.OAuthConnectionRepository
import com.ithersta.anketa.auth.data.repositories.UserRepository
import com.ithersta.anketa.auth.data.tables.OAuthConnectionEntity
import com.ithersta.anketa.auth.data.tables.OAuthConnectionId
import com.ithersta.anketa.auth.data.tables.UserEntity
import org.springframework.stereotype.Service

@Service
class OAuthService(
    private val oAuthConnectionRepository: OAuthConnectionRepository,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
) {
    suspend fun getToken(provider: OAuthProvider, providerUserId: String): String {
        val connectionId = OAuthConnectionId(provider, providerUserId)
        val user = oAuthConnectionRepository.findById(connectionId) ?: run {
            val user = userRepository.save(UserEntity())
            oAuthConnectionRepository.save(OAuthConnectionEntity(provider, providerUserId, user.id!!))
        }
        return jwtService.generateToken(user.userId)
    }
}
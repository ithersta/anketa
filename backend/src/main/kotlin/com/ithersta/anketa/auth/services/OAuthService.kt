package com.ithersta.anketa.auth.services

import com.ithersta.anketa.auth.OAuthProvider
import com.ithersta.anketa.auth.data.repositories.OAuthConnectionRepository
import com.ithersta.anketa.auth.data.repositories.UserRepository
import com.ithersta.anketa.auth.data.tables.OAuthConnectionEntity
import com.ithersta.anketa.auth.data.tables.UserEntity
import org.springframework.stereotype.Service

@Service
class OAuthService(
    private val oAuthConnectionRepository: OAuthConnectionRepository,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val profileService: ProfileService,
) {
    suspend fun getToken(provider: OAuthProvider, providerUserId: String, getUserEntity: () -> UserEntity): String {
        val user = oAuthConnectionRepository.findByProviderAndProviderUserId(provider, providerUserId) ?: run {
            val user = userRepository.save(getUserEntity())
            oAuthConnectionRepository.save(OAuthConnectionEntity(provider, providerUserId, user.id!!))
        }
        val profile = profileService.get(user.userId)!!
        return jwtService.generateToken(user.userId, profile.email)
    }
}

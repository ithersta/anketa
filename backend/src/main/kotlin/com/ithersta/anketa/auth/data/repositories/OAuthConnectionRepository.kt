package com.ithersta.anketa.auth.data.repositories

import com.ithersta.anketa.auth.OAuthProvider
import com.ithersta.anketa.auth.data.tables.OAuthConnectionEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface OAuthConnectionRepository : CoroutineCrudRepository<OAuthConnectionEntity, UUID> {
    suspend fun findByProviderAndProviderUserId(provider: OAuthProvider, providerUserId: String): OAuthConnectionEntity?
}
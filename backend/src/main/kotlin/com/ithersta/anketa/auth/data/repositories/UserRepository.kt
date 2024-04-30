package com.ithersta.anketa.auth.data.repositories

import com.ithersta.anketa.auth.data.tables.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface UserRepository : CoroutineCrudRepository<UserEntity, UUID> {
    suspend fun findByEmailIgnoreCase(email: String): UserEntity?
}

package com.ithersta.anketa.auth.services

import com.ithersta.anketa.auth.data.repositories.UserRepository
import com.ithersta.anketa.auth.domain.Profile
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileService(
    private val userRepository: UserRepository
) {
    suspend fun get(id: UUID) = userRepository.findById(id)?.let {
        Profile(displayName = it.displayName, email = it.email)
    }
}
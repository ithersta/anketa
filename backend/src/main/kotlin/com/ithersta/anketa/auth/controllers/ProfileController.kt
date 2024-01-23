package com.ithersta.anketa.auth.controllers

import com.ithersta.anketa.auth.domain.Profile
import com.ithersta.anketa.auth.services.ProfileService
import com.ithersta.anketa.auth.userId
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ProfileController(
    private val profileService: ProfileService,
) {
    @GetMapping("/profile")
    suspend fun profile(token: UsernamePasswordAuthenticationToken): ResponseEntity<Profile> {
        return ResponseEntity.ofNullable(profileService.get(token.userId))
    }
}
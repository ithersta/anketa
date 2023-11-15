package com.ithersta.anketa.auth.controllers

import com.ithersta.anketa.auth.services.YandexOAuthService
import kotlinx.serialization.Serializable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Serializable
class TokenBody(
    val token: String,
)

@RestController
class OAuthController(
    private val yandexOAuthService: YandexOAuthService,
) {
    @PostMapping("/oauth/yandex")
    suspend fun oauthYandex(@RequestBody body: TokenBody): TokenBody {
        val token = yandexOAuthService.getToken(body.token)
        return TokenBody(token)
    }
}
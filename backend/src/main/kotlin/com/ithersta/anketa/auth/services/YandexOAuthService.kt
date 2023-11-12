package com.ithersta.anketa.auth.services

import com.ithersta.anketa.auth.OAuthProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Serializable
private class YandexOAuthResponse(
    @SerialName("client_id")
    val clientId: String,
    val id: String
)

@Service
class YandexOAuthService(
    private val oAuthService: OAuthService,
    private val secretService: SecretService,
) {
    private val restTemplate = RestTemplate()
    private val json = Json { ignoreUnknownKeys = true }

    fun getToken(yandexToken: String): String {
        val providerUserId = getProviderUserId(yandexToken)
        return oAuthService.getToken(OAuthProvider.Yandex, providerUserId)
    }

    private fun getProviderUserId(yandexToken: String): String {
        val headers = HttpHeaders()
        headers.set("Authorization", "OAuth $yandexToken")
        val request = HttpEntity<String>(headers)
        val responseEntity =
            restTemplate.exchange("https://login.yandex.ru/info", HttpMethod.GET, request, String::class.java)
        val response = json.decodeFromString<YandexOAuthResponse>(responseEntity.body!!)
        require(response.clientId == secretService.secrets.yandexClientId)
        return response.id
    }
}
package com.ithersta.anketa.auth.services

import com.ithersta.anketa.auth.OAuthProvider
import com.ithersta.anketa.auth.data.tables.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Serializable
private class YandexOAuthResponse(
    @SerialName("client_id")
    val clientId: String,
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("default_email")
    val defaultEmail: String,
)

@Component
@ConfigurationProperties("oauth.yandex")
class YandexOauthProperties {
    var clientId: String = ""
    var endpoint: String = "https://login.yandex.ru/info"
}

@Service
class YandexOAuthService(
    private val oAuthService: OAuthService,
    private val yandexOauthProperties: YandexOauthProperties,
) {
    private val webClient = WebClient.create()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getToken(yandexToken: String): String {
        val providerUserData = getProviderUserData(yandexToken)
        return oAuthService.getToken(OAuthProvider.Yandex, providerUserData.id) {
            UserEntity(providerUserData.displayName, providerUserData.defaultEmail)
        }
    }

    private suspend fun getProviderUserData(yandexToken: String): YandexOAuthResponse {
        val responseBody = webClient.get()
            .uri(yandexOauthProperties.endpoint)
            .headers {
                it.set("Authorization", "OAuth $yandexToken")
            }
            .retrieve()
            .awaitBody<String>()
        val response = json.decodeFromString<YandexOAuthResponse>(responseBody)
        require(response.clientId == yandexOauthProperties.clientId)
        return response
    }
}

package com.ithersta.anketa.gpt

import io.netty.handler.ssl.SslContextBuilder
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.netty.http.client.HttpClient
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.time.Instant
import java.util.UUID
import javax.net.ssl.TrustManagerFactory

@Serializable
private class TokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_at")
    val expiresAt: Long,
)

@Serializable
private class TextGenRequest(
    val messages: List<Message>,
    val model: String = "GigaChat:latest",
    val temperature: Double = 1.0,
    @SerialName("top_p")
    val topP: Double = 0.1,
    val n: Int = 1,
    val stream: Boolean = false,
    @SerialName("max_tokens")
    val maxTokens: Int = 512,
    val repetitionPenalty: Int = 1,
) {
    @Serializable
    class Message(
        val content: String,
        val role: Role,
    )

    @Serializable
    enum class Role {
        @SerialName("user")
        User,
        @SerialName("assistant")
        Assistant,
    }
}

@Serializable
private class TextGenResponse(
    val choices: List<Choice>
) {
    @Serializable
    class Choice(
        val message: TextGenRequest.Message
    )
}

@Service
class GigaChatApi(
    gigaChatProperties: GigaChatProperties,
    webClientFactory: WebClientFactory,
) : GptApi {
    private val authSecret = File(gigaChatProperties.authSecretFile).readText().trim()
    private val lock = Mutex()
    private val webClient = webClientFactory.build()
    private var tokenResponse: TokenResponse? = null
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override suspend fun complete(text: String): String {
        val token = getToken()
        val requestBody = TextGenRequest(listOf(TextGenRequest.Message(text, TextGenRequest.Role.User)))
        val responseBody = webClient.post()
            .uri("https://gigachat.devices.sberbank.ru/api/v1/chat/completions")
            .headers {
                it.contentType = MediaType.APPLICATION_JSON
                it.accept = listOf(MediaType.APPLICATION_JSON)
                it.setBearerAuth(token)
            }
            .bodyValue(json.encodeToString(requestBody))
            .retrieve()
            .awaitBody<String>()
        val response = json.decodeFromString<TextGenResponse>(responseBody)
        return response.choices.first().message.content
    }

    private suspend fun getToken(): String = lock.withLock {
        val tokenValue = tokenResponse
        if (tokenValue != null && Instant.now().isBefore(Instant.ofEpochSecond(tokenValue.expiresAt))) {
            return@withLock tokenValue.accessToken
        }
        val responseBody = webClient.post()
            .uri("https://ngw.devices.sberbank.ru:9443/api/v2/oauth")
            .headers {
                it.contentType = MediaType.APPLICATION_FORM_URLENCODED
                it.accept = listOf(MediaType.APPLICATION_JSON)
                it.set("RqUID", UUID.randomUUID().toString())
                it.setBasicAuth(authSecret)
            }
            .bodyValue("scope=GIGACHAT_API_PERS")
            .retrieve()
            .awaitBody<String>()
        val response = json.decodeFromString<TokenResponse>(responseBody)
        tokenResponse = response
        response.accessToken
    }
}

package com.ithersta.anketa.gpt

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.io.File

@Component
@ConfigurationProperties("yandex-gpt")
class YandexGptProperties {
    var iamFile: String = ""
    var folderId: String = ""
}

@Serializable
data class YandexGptRequest(
    val modelUri: String,
    val completionOptions: CompletionOptions,
    val messages: List<Message>,
) {
    @Serializable
    data class Message(
        val role: Role,
        val text: String,
    )

    @Serializable
    enum class Role {
        @SerialName("system")
        System,

        @SerialName("user")
        User,

        @SerialName("assistant")
        Assistant,
    }

    @Serializable
    data class CompletionOptions(
        val stream: Boolean = false,
        val temperature: Double = 0.3,
        val maxTokens: Int = 8000,
    )
}

@Serializable
data class YandexGptResponse(
    val alternatives: List<Alternative>,
) {
    @Serializable
    data class Alternative(
        val message: YandexGptRequest.Message,
    )
}

@Serializable
data class YandexGptOperation(
    val id: String,
    val done: Boolean,
    val response: YandexGptResponse? = null,
)

@Service
class YandexGptApi(
    properties: YandexGptProperties,
) {
    private val token = File(properties.iamFile).readText().trim()
    private val folderId = properties.folderId
    private val webClient = WebClient.builder()
        .filter(fixContentType())
        .baseUrl("https://llm.api.cloud.yandex.net")
        .defaultHeader("Authorization", "Bearer $token")
        .defaultHeader("Content-Type", "application/json")
        .defaultHeader("Accept", "application/json")
        .build()
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun createRequest(
        systemMessage: String,
        userMessage: String,
        completionOptions: YandexGptRequest.CompletionOptions = YandexGptRequest.CompletionOptions(),
        modelUri: String = "yandexgpt-pro",
    ): YandexGptRequest = YandexGptRequest(
        modelUri = "gpt://${folderId}/${modelUri}",
        completionOptions = completionOptions,
        messages = listOf(
            YandexGptRequest.Message(
                role = YandexGptRequest.Role.System,
                text = systemMessage,
            ),
            YandexGptRequest.Message(
                role = YandexGptRequest.Role.User,
                text = userMessage,
            ),
        )
    )

    suspend fun sendAsync(request: YandexGptRequest): String {
        val requestJson = json.encodeToString(request)
        val responseBody = webClient.post()
            .uri("foundationModels/v1/completionAsync")
            .header("x-folder-id", folderId)
            .bodyValue(requestJson)
            .exchangeToMono { response ->
                if (response.statusCode().is2xxSuccessful) {
                    response.bodyToMono<String>()
                } else {
                    response.bodyToMono<String>().flatMap { Mono.error(Exception(it)) }
                }
            }
            .awaitSingle()

        val operation = json.decodeFromString<YandexGptOperation>(responseBody)
        return operation.id
    }

    suspend fun getAsync(id: String): YandexGptOperation {
        val responseBody = webClient.get()
            .uri("operations/${id}")
            .retrieve()
            .awaitBody<String>()
        return json.decodeFromString<YandexGptOperation>(responseBody)
    }

    private fun fixContentType(): ExchangeFilterFunction {
        return ExchangeFilterFunction { request, next ->
            next.exchange(request).flatMap { response ->
                val fixedResponse = response.mutate().headers {
                    it.remove("Content-Type")
                    it.add("Content-Type", "application/json")
                }.build()
                Mono.just(fixedResponse)
            }
        }
    }
}

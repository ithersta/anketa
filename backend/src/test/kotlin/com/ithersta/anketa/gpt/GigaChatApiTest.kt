package com.ithersta.anketa.gpt

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class GigaChatApiTest {
    private val gigaChatApi = GigaChatApi(GigaChatProperties().apply { authSecretFile = "gigachat-auth" }, WebClientFactory())

    @Test
    fun `complete should generate completed text`(): Unit = runBlocking {
        val completion = gigaChatApi.complete("Ты кто?")
        println(completion)
    }
}

package com.ithersta.anketa.gpt

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class YandexGptApiTest {
    @Autowired
    private lateinit var api: YandexGptApi

    @Test
    fun `YandexGpt should complete successfully`() = runBlocking {
        val id = api.sendAsync(api.createRequest("Сократи следующее предложение", "Привет всем"))
        repeat(10) {
            delay(500L)
            val operation = api.getAsync(id)
            if (operation.done) {
                println(operation)
                return@runBlocking
            }
        }
    }
}

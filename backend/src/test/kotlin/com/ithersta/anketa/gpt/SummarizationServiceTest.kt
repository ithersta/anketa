package com.ithersta.anketa.gpt

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SummarizationServiceTest {
    @Autowired
    private lateinit var summarizationService: SummarizationService

    @Test
    fun `SummarizationService should return the correct result`() = runBlocking {
        val summary = summarizationService.summarize(listOf(
            "Хочу математикууууу!",
            "Я УСТАЛ",
            "Я думаю, мне не хватает физики",
            "ВСË СУППЕР",
            "ыавфыаыфвавфы",
        ))
        println(summary)
    }
}

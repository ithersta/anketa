package com.ithersta.anketa.gpt

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class SummarizationService(
    private val gptApi: YandexGptApi,
) {
    suspend fun summarize(question: String, answers: List<String>): String {
        val id = gptApi.sendAsync(gptApi.createRequest(
            systemMessage = "Проанализируй ответы на вопрос анкеты. На основании этого анализа сделай вывод о тональности всех ответов в целом: позитивная, негативная, нейтральная. После этого, если они есть, перечисли список пожеланий респондентов.",
            userMessage = "Вопрос:\n$question\n\nОтветы:\n${answers.joinToString("\n")}"
        ))
        for (i in 0..30) {
            delay(500)
            val response = gptApi.getAsync(id).response
            if (response != null) {
                return response.alternatives.first().message.text
            }
        }
        error("Couldn't summarize")
    }
}

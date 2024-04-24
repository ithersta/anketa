package com.ithersta.anketa.gpt

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class SummarizationService(
    private val gptApi: YandexGptApi,
) {
    suspend fun summarize(answers: List<String>): String {
        val id = gptApi.sendAsync(gptApi.createRequest(
            systemMessage = "Проанализируй отзывы. На основании этого анализа сделай вывод о тональности всего обсуждения: позитивная, негативная, нейтральная.\nПосле этого перечисли список пожеланий пользователей.",
            userMessage = answers.joinToString("\n")
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

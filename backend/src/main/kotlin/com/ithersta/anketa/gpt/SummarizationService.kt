package com.ithersta.anketa.gpt

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

private const val SystemMessage = """Проанализируй ответы на вопрос анкеты. На основании этого анализа сделай вывод о тональности всего обсуждения: позитивная, негативная, нейтральная. После этого перескажи тезисы респондентов. Игнорируй непонятные ответы.

Формат твоего ответа:
```
Тональность: ...
Тезисы:
...
Можно сделать вывод...
```"""

@Service
class SummarizationService(
    private val gptApi: YandexGptApi,
) {
    suspend fun summarize(question: String, answers: List<String>): String {
        val id = gptApi.sendAsync(gptApi.createRequest(
            systemMessage = SystemMessage,
            userMessage = "Вопрос:\n$question\n\nОтветы:\n${answers.joinToString("\n")}"
        ))
        for (i in 0..120) {
            delay(500)
            val response = gptApi.getAsync(id).response
            if (response != null) {
                return response.alternatives.first().message.text
            }
        }
        error("Couldn't summarize")
    }
}

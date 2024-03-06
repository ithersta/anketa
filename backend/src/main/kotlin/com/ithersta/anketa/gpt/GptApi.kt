package com.ithersta.anketa.gpt

interface GptApi {
    suspend fun complete(text: String): String
}

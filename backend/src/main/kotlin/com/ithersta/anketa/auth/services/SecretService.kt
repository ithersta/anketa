package com.ithersta.anketa.auth.services

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.io.File

@Serializable
class Secrets(
    @SerialName("jwt_secret")
    val jwtSecret: ByteArray,
)

@Service
class SecretService(
    val path: String = "secrets.json"
) {
    val secrets: Secrets by lazy {
        Json.decodeFromString(File(path).readText())
    }
}
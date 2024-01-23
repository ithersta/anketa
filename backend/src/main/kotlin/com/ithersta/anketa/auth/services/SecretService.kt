package com.ithersta.anketa.auth.services

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.io.File
import java.util.Base64

class Secrets(
    val jwtSecret: ByteArray,
)

@Component
@ConfigurationProperties("secrets")
class SecretsProperties {
    var jwtSecretFile: String = ""
}

@Service
class SecretService(
    private val secretsProperties: SecretsProperties
) {
    val secrets = run {
        val secretFile = File(secretsProperties.jwtSecretFile)
        Secrets(jwtSecret = Base64.getDecoder().decode(secretFile.readText().trim()))
    }
}
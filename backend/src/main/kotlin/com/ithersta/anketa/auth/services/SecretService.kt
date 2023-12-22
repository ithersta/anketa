package com.ithersta.anketa.auth.services

import org.springframework.stereotype.Service
import java.io.File
import java.util.Base64

class Secrets(
    val jwtSecret: ByteArray,
)

@Service
class SecretService {
    val secrets = run {
        val secretFile = File(System.getenv("JWT_SECRET_FILE"))
        Secrets(jwtSecret = Base64.getDecoder().decode(secretFile.readText().trim()))
    }
}
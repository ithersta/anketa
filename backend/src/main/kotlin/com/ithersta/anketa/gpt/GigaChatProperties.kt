package com.ithersta.anketa.gpt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.stereotype.Component
import kotlin.time.Duration.Companion.minutes

@Component
@ConfigurationProperties(prefix = "gigachat")
class GigaChatProperties {
    lateinit var authSecretFile: String
}

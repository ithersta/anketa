package com.ithersta.anketa.auth.services

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
@ConfigurationProperties("whitelist")
class WhitelistProperties {
    var emails: Set<String> = emptySet()
}

@Service
class WhitelistService(
    properties: WhitelistProperties
) {
    private val normalizedEmails = properties.emails.asSequence()
        .map { email -> email.lowercase() }
        .toSet()

    fun isAllowed(email: String): Boolean {
        return email.lowercase() in normalizedEmails
    }
}

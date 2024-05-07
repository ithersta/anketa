package com.ithersta.anketa.auth.controllers

import com.ithersta.anketa.DatabaseTest
import com.ithersta.anketa.auth.domain.Profile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class ProfileControllerTest : DatabaseTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `given token when GET profile then profile is returned`() {
        webTestClient.get()
            .uri("/profile")
            .header("Authorization", "Bearer ${populator.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(Profile::class.java)
    }
}

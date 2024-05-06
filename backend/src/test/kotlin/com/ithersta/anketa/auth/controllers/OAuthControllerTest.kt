package com.ithersta.anketa.auth.controllers

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.ithersta.anketa.DatabaseTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class OAuthControllerTest : DatabaseTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    private lateinit var wireMockServer: WireMockServer

    @BeforeEach
    fun setup() {
        wireMockServer = WireMockServer(wireMockConfig().port(8089))
        wireMockServer.start()
        configureFor("localhost", wireMockServer.port())
        stubFor(get(urlEqualTo("/info"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "client_id": "123",
                      "id": "yandex-id",
                      "display_name": "Yandex Display Name",
                      "default_email": "email@yandex.ru"
                    }
                """.trimIndent())
                .withStatus(200)))
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `given yandex token when oauth-yandex then token is returned`() {
        val requestBody = TokenBody("sample")

        webTestClient.post()
            .uri("/oauth/yandex")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isOk
            .expectBody(TokenBody::class.java)
    }
}

package com.ithersta.anketa.survey.report

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.ithersta.anketa.DatabaseTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.UUID

@SpringBootTest
@AutoConfigureWebTestClient
class ReportControllerTest : DatabaseTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    private lateinit var wireMockServer: WireMockServer

    @BeforeEach
    fun setup() {
        wireMockServer = WireMockServer(wireMockConfig().port(8089))
        wireMockServer.start()
        configureFor("localhost", wireMockServer.port())
        stubFor(
            any(anyUrl())
            .willReturn(
                aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                      "id": "123",
                      "done": true,
                      "response": {
                        "alternatives": [{
                          "message": {
                            "role": "assistant",
                            "text": "Result"
                          }
                        }]
                      }
                    }
                """.trimIndent())
                .withStatus(200)))
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.stop()
    }

    @Test
    fun `given valid report content when POST export-report then report is exported`() {
        webTestClient.post()
            .uri("/dashboard/survey/${populator.surveyId}/export/report")
            .header("Authorization", "Bearer ${populator.token}")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(populator.sampleReport)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `given token when POST summarise then summarization is saved`() {
        webTestClient.post()
            .uri("/dashboard/survey/${populator.surveyId}/export/summarise/${UUID(0, 3)}")
            .header("Authorization", "Bearer ${populator.token}")
            .exchange()
            .expectStatus().isOk
    }
}

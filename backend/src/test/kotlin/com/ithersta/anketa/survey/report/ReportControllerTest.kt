package com.ithersta.anketa.survey.report

import com.ithersta.anketa.DatabaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class ReportControllerTest : DatabaseTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

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
}

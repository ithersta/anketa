package com.ithersta.anketa.survey.exporters

import com.ithersta.anketa.DatabaseTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class ExportControllerTest : DatabaseTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `given token when GET export-csv then CSV is exported`() {
        webTestClient.get()
            .uri("/dashboard/survey/${populator.surveyId}/export/csv")
            .header("Authorization", "Bearer ${populator.token}")
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `given token when GET export-xlsx then XLSX is exported`() {
        webTestClient.get()
            .uri("/dashboard/survey/${populator.surveyId}/export/xlsx")
            .header("Authorization", "Bearer ${populator.token}")
            .exchange()
            .expectStatus().isOk
    }
}

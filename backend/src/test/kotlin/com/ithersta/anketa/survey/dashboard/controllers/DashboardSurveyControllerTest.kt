package com.ithersta.anketa.survey.dashboard.controllers

import com.ithersta.anketa.DatabaseTest
import com.ithersta.anketa.survey.dashboard.dto.DashboardDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class DashboardSurveyControllerTest : DatabaseTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `given valid survey content when POST dashboard-survey then survey is created`() {
        webTestClient.post()
            .uri("/dashboard/survey")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer ${populator.token}")
            .bodyValue(populator.sampleSurveyContent)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun `given token when GET dashboard-survey then surveys are returned`() {
        webTestClient.get()
            .uri("/dashboard/survey")
            .header("Authorization", "Bearer ${populator.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody(DashboardDto::class.java)
    }
}

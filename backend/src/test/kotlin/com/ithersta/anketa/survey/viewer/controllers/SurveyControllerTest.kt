package com.ithersta.anketa.survey.viewer.controllers

import com.ithersta.anketa.DatabaseTest
import com.ithersta.anketa.Populator
import com.ithersta.anketa.survey.domain.SurveyContent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class SurveyControllerTest : DatabaseTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun `given no token when GET survey then survey is returned`() {
        webTestClient.get()
            .uri("/survey/${populator.surveyId}")
            .exchange()
            .expectStatus().isOk
            .expectBody(SurveyContent::class.java)
    }
}

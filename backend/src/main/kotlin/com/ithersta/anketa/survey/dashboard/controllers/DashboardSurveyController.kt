package com.ithersta.anketa.survey.dashboard.controllers

import arrow.core.Either
import com.ithersta.anketa.auth.userId
import com.ithersta.anketa.survey.dashboard.dto.DashboardSurveyDto
import com.ithersta.anketa.survey.dashboard.services.DashboardSurveyService
import com.ithersta.anketa.survey.domain.SurveyContent
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/dashboard/survey")
class DashboardSurveyController(
    private val surveyService: DashboardSurveyService,
) {
    @PostMapping
    suspend fun add(
        @RequestBody content: SurveyContent,
        token: UsernamePasswordAuthenticationToken
    ): ResponseEntity<String> {
        return when (val result = surveyService.add(content, token.userId)) {
            is Either.Left -> ResponseEntity.badRequest()
                .body(result.value.joinToString(separator = "\n") { it.message })

            is Either.Right -> ResponseEntity.ok(result.value.toString())
        }
    }

    @GetMapping
    suspend fun getAll(token: UsernamePasswordAuthenticationToken): List<DashboardSurveyDto> =
        surveyService.getAll(token.userId)
}

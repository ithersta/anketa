package com.ithersta.anketa.survey.dashboard.controllers

import arrow.core.Either
import com.ithersta.anketa.auth.userId
import com.ithersta.anketa.survey.dashboard.dto.DashboardSurveyDto
import com.ithersta.anketa.survey.dashboard.services.DashboardSurveyService
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.viewer.services.SurveyService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/dashboard/survey")
class DashboardSurveyController(
    private val surveyService: DashboardSurveyService,
) {
    @PostMapping
    suspend fun add(@RequestBody content: SurveyContent, token: UsernamePasswordAuthenticationToken): ResponseEntity<String> {
        return when(val result = surveyService.add(content, token.userId)) {
            is Either.Left -> ResponseEntity.badRequest()
                .body(result.value.joinToString(separator = "\n") { it.message })
            is Either.Right -> ResponseEntity.ok(result.value.toString())
        }
    }

    @GetMapping
    suspend fun getAll(token: UsernamePasswordAuthenticationToken): List<DashboardSurveyDto> =
        surveyService.getAll(token.userId)

    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: UUID, token: UsernamePasswordAuthenticationToken) =
        ResponseEntity.ofNullable(surveyService.get(id, token.userId))
}
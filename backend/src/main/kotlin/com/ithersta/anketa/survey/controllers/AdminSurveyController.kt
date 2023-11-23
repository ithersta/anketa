package com.ithersta.anketa.survey.controllers

import arrow.core.Either
import arrow.core.left
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.services.SurveyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/survey")
class AdminSurveyController(
    private val surveyService: SurveyService,
) {
    @PostMapping
    suspend fun add(@RequestBody content: SurveyContent): ResponseEntity<String> {
        return when(val result = surveyService.add(content)) {
            is Either.Left -> ResponseEntity.badRequest().body(result.left().toString())
            is Either.Right -> ResponseEntity.ok().build()
        }
    }
}
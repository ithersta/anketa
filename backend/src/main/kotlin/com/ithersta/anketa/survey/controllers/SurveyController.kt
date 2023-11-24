package com.ithersta.anketa.survey.controllers

import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.MultiChoiceEntry
import com.ithersta.anketa.survey.domain.entries.PolarChoiceEntry
import com.ithersta.anketa.survey.domain.entries.TextEntry
import com.ithersta.anketa.survey.domain.entries.TextFieldEntry
import com.ithersta.anketa.survey.services.SurveyService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/survey")
class SurveyController(
    private val surveyService: SurveyService,
) {
    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: UUID): ResponseEntity<SurveyContent> =
        ResponseEntity.ofNullable(surveyService.getContentById(id))
}
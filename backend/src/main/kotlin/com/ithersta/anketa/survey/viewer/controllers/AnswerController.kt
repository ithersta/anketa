package com.ithersta.anketa.survey.viewer.controllers

import com.ithersta.anketa.crypto.SignedMessage
import com.ithersta.anketa.survey.viewer.services.AnswerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/answer")
class AnswerController(
    private val answerService: AnswerService,
) {
    @PostMapping("/{id}")
    suspend fun post(
        @PathVariable id: UUID,
        @RequestBody signedAnswers: SignedMessage,
    ): ResponseEntity<String> {
        return when (val result = answerService.add(id, signedAnswers)) {
            is AnswerService.AddResult.InvalidAnswers -> ResponseEntity.badRequest()
                .body(result.errors.joinToString(separator = "\n") { it.message })
            AnswerService.AddResult.InvalidSignature -> ResponseEntity.badRequest().body("Invalid signature")
            AnswerService.AddResult.InvalidSurveyId -> ResponseEntity.badRequest().body("Invalid survey id")
            AnswerService.AddResult.OK -> ResponseEntity.ok().build()
        }
    }
}
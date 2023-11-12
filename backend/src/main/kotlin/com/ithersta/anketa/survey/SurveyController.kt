package com.ithersta.anketa.survey

import com.ithersta.anketa.survey.domain.Survey
import com.ithersta.anketa.survey.domain.entries.TextEntry
import jakarta.websocket.server.PathParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/survey")
class SurveyController {
    @GetMapping
    fun get(@PathParam("uuid") uuid: String): Survey =
        Survey("Анкета", listOf(TextEntry(1, uuid)))
}
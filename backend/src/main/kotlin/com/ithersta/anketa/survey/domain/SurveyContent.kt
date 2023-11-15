package com.ithersta.anketa.survey.domain

import com.ithersta.anketa.survey.domain.entries.RequiresAnswer
import com.ithersta.anketa.survey.domain.entries.SurveyAnswer
import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import kotlinx.serialization.Serializable

@Serializable
data class SurveyContent(
    val title: String,
    val entries: List<SurveyEntry>,
)

fun SurveyContent.isAnswerValid(answers: Map<Int, SurveyAnswer>): Boolean =
    entries.asSequence()
        .filterIsInstance<RequiresAnswer>()
        .all {
            val answer = answers[it.id] ?: return@all false
            it.isValid(answer)
        }

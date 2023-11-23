package com.ithersta.anketa.survey.domain.entries

import kotlinx.serialization.Serializable

@Serializable
sealed interface RequiresAnswer : SurveyEntry {
    val isOptional: Boolean
    fun isAnswerValid(answer: SurveyAnswer): Boolean
}
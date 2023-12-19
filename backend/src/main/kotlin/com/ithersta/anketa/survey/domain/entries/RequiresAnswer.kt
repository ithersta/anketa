package com.ithersta.anketa.survey.domain.entries

import kotlinx.serialization.Serializable

@Serializable
sealed interface RequiresAnswer : SurveyEntry {
    val isRequired: Boolean
    fun validateAnswer(answer: SurveyAnswer): List<SurveyAnswer.ValidationError>
}
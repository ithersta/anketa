package com.ithersta.anketa.survey.domain.entries

import kotlinx.serialization.Serializable

@Serializable
sealed interface RequiresAnswer : SurveyEntry {
    fun isValid(answer: SurveyAnswer): Boolean
}
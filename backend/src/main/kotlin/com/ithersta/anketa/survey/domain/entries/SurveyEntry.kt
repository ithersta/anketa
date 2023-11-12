package com.ithersta.anketa.survey.domain.entries

import kotlinx.serialization.Serializable

@Serializable
sealed interface SurveyEntry {
    val id: Int
    fun isValid(): Boolean
}

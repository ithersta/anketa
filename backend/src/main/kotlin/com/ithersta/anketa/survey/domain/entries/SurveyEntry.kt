package com.ithersta.anketa.survey.domain.entries

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed interface SurveyEntry {
    sealed interface ValidationError {
        val message: String
    }

    var id: UUID
    fun validate(): List<ValidationError>
}

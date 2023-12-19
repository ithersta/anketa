package com.ithersta.anketa.survey.domain.entries

import kotlinx.serialization.Serializable

@Serializable
sealed interface SurveyAnswer {
    sealed interface ValidationError {
        val message: String

        abstract class InvalidType : ValidationError {
            override val message: String
                get() = "Invalid answer type"
        }
    }
}

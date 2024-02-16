package com.ithersta.anketa.survey.report.entries

import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed interface ReportEntry {
    sealed interface ValidationError {
        val message: String
    }

    var id: UUID
    val forEntryWithId: UUID?
    fun validate(surveyEntry: SurveyEntry?): List<ValidationError>
}
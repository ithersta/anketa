package com.ithersta.anketa.survey.report.entries

import com.ithersta.anketa.serialization.UuidSerializer
import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class TextReportEntry(
    override var id: @Serializable(with = UuidSerializer::class) UUID,
    val content: String,
) : ReportEntry {
    override val forEntryWithId: UUID? get() = null

    override fun validate(surveyEntry: SurveyEntry?): List<ReportEntry.ValidationError> {
        return emptyList()
    }
}
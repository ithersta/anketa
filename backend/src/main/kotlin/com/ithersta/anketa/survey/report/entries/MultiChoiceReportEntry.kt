package com.ithersta.anketa.survey.report.entries

import com.ithersta.anketa.serialization.UuidSerializer
import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class MultiChoiceReportEntry(
    override var id: @Serializable(with = UuidSerializer::class) UUID,
    override val forEntryWithId: @Serializable(with = UuidSerializer::class) UUID,
    val text: String,
) : ReportEntry {
    override fun validate(surveyEntry: SurveyEntry?): List<ReportEntry.ValidationError> {
        TODO("Not yet implemented")
    }
}

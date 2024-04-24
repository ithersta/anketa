package com.ithersta.anketa.survey.report.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("Text")
class TextReportEntry(
    val content: String,
) : ReportEntry {
    override val forEntryWithId: UUID? get() = null
}

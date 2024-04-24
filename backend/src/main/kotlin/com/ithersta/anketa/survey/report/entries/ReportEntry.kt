package com.ithersta.anketa.survey.report.entries

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed interface ReportEntry {
    val forEntryWithId: UUID?
}

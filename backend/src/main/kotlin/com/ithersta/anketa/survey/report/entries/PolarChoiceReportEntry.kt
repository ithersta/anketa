package com.ithersta.anketa.survey.report.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("PolarChoice")
class PolarChoiceReportEntry(
    override val forEntryWithId: @Serializable(with = UuidSerializer::class) UUID,
    val template: String,
) : ReportEntry

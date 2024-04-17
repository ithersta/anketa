package com.ithersta.anketa.survey.report

import com.ithersta.anketa.serialization.UuidSerializer
import com.ithersta.anketa.survey.report.entries.ReportEntry
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class ReportContent(
    val entries: List<ReportEntry>,
    val divideBy: @Serializable(with = UuidSerializer::class) UUID? = null,
)

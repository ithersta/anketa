package com.ithersta.anketa.survey.report.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("MultiChoice")
class MultiChoiceReportEntry(
    override val forEntryWithId: @Serializable(with = UuidSerializer::class) UUID,
    val template: String,
    val doSummarise: Boolean,
    val chartType: ChartType? = null,
) : ReportEntry {
    enum class ChartType {
        Bar, Pie
    }
}

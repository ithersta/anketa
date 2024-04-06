package com.ithersta.anketa.survey.report.exporters

import com.ithersta.anketa.survey.domain.AnswerMap
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.MultiChoiceEntry
import com.ithersta.anketa.survey.report.DividedReport
import com.ithersta.anketa.survey.report.ReportContent
import com.ithersta.anketa.survey.report.ReportEntrySummary
import com.ithersta.anketa.survey.report.entries.MultiChoiceReportEntry
import com.ithersta.anketa.survey.report.generateDividedReports
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

class DocxReportExporterTest {
    private val docxReportExporter = DocxReportExporter()

    @Test
    fun exportSample() {
        val reportContent = ReportContent(
            entries = listOf(
                MultiChoiceReportEntry(
                    id = UUID.randomUUID(),
                    forEntryWithId = UUID(0, 1),
                    template = "\$t1 = \$c1, \$t2 = \$c2, sum = \${c1 + c2}",
                )
            ),
            divideBy = null
        )
        val surveyContent = SurveyContent(
            title = "Title",
            entries = listOf(
                MultiChoiceEntry(
                    id = UUID(0, 1),
                    isRequired = true,
                    question = "Как дела?",
                    options = listOf(
                        "Хорошо",
                        "Не очень",
                    ),
                    isAcceptingOther = false,
                    minSelected = 1,
                    maxSelected = 1,
                )
            )
        )
        val answers = listOf(
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(1), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(0), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(0), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(0), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(0), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(0), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(0), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(1), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(1), null))),
            AnswerMap(mapOf(UUID(0, 1) to MultiChoiceEntry.Answer(setOf(1), null))),
        )
        val dividedReport = generateDividedReports(
            reportContent = reportContent,
            surveyContent = surveyContent,
            answers = answers,
        ).single()
        File("Sample.docx").writeBytes(docxReportExporter.generateFile(dividedReport))
    }
}

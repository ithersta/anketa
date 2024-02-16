package com.ithersta.anketa.survey.report.exporters

import com.ithersta.anketa.survey.report.DividedReport
import com.ithersta.anketa.survey.report.ReportEntrySummary
import org.junit.jupiter.api.Test
import java.io.File

class DocxReportExporterTest {
    private val docxReportExporter = DocxReportExporter()

    @Test
    fun exportSample() {
        val dividedReport = DividedReport(
            name = "Sample",
            summaries = listOf(
                ReportEntrySummary.Text(
                    "Пример"
                ),
                ReportEntrySummary.MultiChoice(
                    question = "Норм?",
                    formattedText = "Отвтеили",
                    answerCount = 164,
                    noAnswerCount = 2,
                    options = listOf(
                        ReportEntrySummary.MultiChoice.Option(
                            text = "Да",
                            count = 120,
                        ),
                        ReportEntrySummary.MultiChoice.Option(
                            text = "Нет",
                            count = 42,
                        )
                    )
                )
            )
        )
        File("Sample.docx").writeBytes(docxReportExporter.generateFile(dividedReport))
    }
}
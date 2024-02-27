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
                    ),
                    isSingleChoice = true,
                ),
                ReportEntrySummary.MultiChoice(
                    question = "Как настроение?",
                    formattedText = "Отвтеили",
                    answerCount = 208,
                    noAnswerCount = 2,
                    options = listOf(
                        ReportEntrySummary.MultiChoice.Option(
                            text = "Отлично",
                            count = 120,
                        ),
                        ReportEntrySummary.MultiChoice.Option(
                            text = "Хорошо",
                            count = 42,
                        ),
                        ReportEntrySummary.MultiChoice.Option(
                            text = "Нормально",
                            count = 44,
                        )
                    ),
                    isSingleChoice = false,
                ),
            )
        )
        File("Sample.docx").writeBytes(docxReportExporter.generateFile(dividedReport))
    }
}

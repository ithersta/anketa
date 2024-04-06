package com.ithersta.anketa.survey.report

import com.ithersta.anketa.exception.ForbiddenException
import com.ithersta.anketa.survey.data.repositories.AnswerRepository
import com.ithersta.anketa.survey.data.tables.AnswerEntity
import com.ithersta.anketa.survey.data.tables.toAnswerMap
import com.ithersta.anketa.survey.report.exporters.DocxReportExporter
import com.ithersta.anketa.survey.viewer.services.SurveyService
import org.apache.commons.io.output.ByteArrayOutputStream
import org.springframework.stereotype.Service
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
class ReportService(
    private val surveyService: SurveyService,
    private val answerRepository: AnswerRepository,
    private val docxReportExporter: DocxReportExporter,
) {
    suspend fun generateDocx(
        surveyId: UUID,
        userId: UUID,
        reportContent: ReportContent,
    ): Pair<String, ByteArray> {
        val surveyContent = surveyService.getContentById(surveyId, userId) ?: throw ForbiddenException()
        val answerMaps = answerRepository.findBySurveyId(surveyId)
            .map(AnswerEntity::toAnswerMap)
        val dividedReports = generateDividedReports(
            reportContent = reportContent,
            surveyContent = surveyContent,
            answers = answerMaps,
        )
        val files = dividedReports.map { dividedReport ->
            "${dividedReport.name}.docx" to docxReportExporter.generateFile(dividedReport)
        }
        return compressIfMoreThanOne(surveyContent.title, files)
    }

    private fun compressIfMoreThanOne(name: String, files: List<Pair<String, ByteArray>>): Pair<String, ByteArray> {
        if (files.size == 1) return files.first()
        return compress(name, files)
    }

    private fun compress(name: String, files: List<Pair<String, ByteArray>>): Pair<String, ByteArray> {
        val bytes = ByteArrayOutputStream().also {
            ZipOutputStream(it).use { zos ->
                files.forEach { (name, content) ->
                    val entry = ZipEntry(name)
                    zos.putNextEntry(entry)
                    zos.write(content)
                    zos.closeEntry()
                }
            }
        }.toByteArray()
        return "${name}.zip" to bytes
    }
}

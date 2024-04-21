package com.ithersta.anketa.survey.exporters

import com.ithersta.anketa.survey.domain.AnswerMap
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.io.StringWriter

fun generateCsv(surveyContent: SurveyContent, answerMaps: Collection<AnswerMap>): ByteArray {
    val requiresAnswer = surveyContent.entries.filterIsInstance<RequiresAnswer>()
    val headers = requiresAnswer.map { entry ->
        entry.toCellText()
    }.toTypedArray()
    val csvFormat = CSVFormat.RFC4180.builder()
        .setHeader(*headers)
        .build()

    val stringWriter = StringWriter()
    CSVPrinter(stringWriter, csvFormat).use { printer ->
        answerMaps.forEach { answerMap ->
            val record = requiresAnswer.mapNotNull { entry ->
                val answer = answerMap.answers[entry.id] ?: return@mapNotNull null
                answer.toCellText(entry)
            }
            printer.printRecord(record)
        }
    }
    return stringWriter.toString().toByteArray()
}


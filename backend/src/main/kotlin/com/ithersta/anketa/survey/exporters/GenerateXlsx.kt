package com.ithersta.anketa.survey.exporters

import com.ithersta.anketa.survey.domain.AnswerMap
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.*
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream

fun generateXlsx(surveyContent: SurveyContent, answerMaps: Collection<AnswerMap>): ByteArray {
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet()
    val requiresAnswer = surveyContent.entries.filterIsInstance<RequiresAnswer>()
    val cellStyle = workbook.createCellStyle()
    cellStyle.wrapText = true
    createHeader(sheet, requiresAnswer)
    answerMaps.forEachIndexed { index, answerMap ->
        val row = sheet.createRow(index + 1)
        fillAnswerRow(row, requiresAnswer, answerMap, cellStyle)
        row.rowStyle = cellStyle
    }
    for (i in requiresAnswer.indices) {
        sheet.autoSizeColumn(i)
    }
    return workbook.toByteArray()
}

private fun fillAnswerRow(
    row: XSSFRow,
    requiresAnswer: List<RequiresAnswer>,
    answerMap: AnswerMap,
    cellStyle: XSSFCellStyle,
) {
    requiresAnswer.forEachIndexed { entryIndex, entry ->
        val answer = answerMap.answers[entry.id] ?: return@forEachIndexed
        val cell = row.createCell(entryIndex, CellType.STRING)
        cell.setCellValue(answer.toCellText(entry))
        cell.cellStyle = cellStyle
    }
}

private fun createHeader(
    sheet: XSSFSheet,
    requiresAnswer: List<RequiresAnswer>,
) {
    val cellStyle = sheet.workbook.createCellStyle()
    cellStyle.wrapText = true
    val header = sheet.createRow(0)
    requiresAnswer.forEachIndexed { index, entry ->
        val cell = header.createCell(index, CellType.STRING)
        cell.setCellValue(entry.toCellText())
        cell.cellStyle = cellStyle
    }
}

private fun XSSFWorkbook.toByteArray() = ByteArrayOutputStream()
    .also { write(it) }
    .toByteArray()

package com.ithersta.anketa.survey.report.exporters

import com.ithersta.anketa.survey.report.DividedReport

interface ReportExporter {
    fun generateFile(dividedReport: DividedReport): ByteArray
}

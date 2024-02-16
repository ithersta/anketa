package com.ithersta.anketa.survey.report.exporters

import com.ithersta.anketa.survey.report.DividedReport
import com.ithersta.anketa.survey.report.ReportEntrySummary
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xddf.usermodel.chart.*
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.ByteArrayOutputStream
import java.io.InputStream

class DocxReportExporter : ReportExporter {
    override fun generateFile(dividedReport: DividedReport): ByteArray {
        return generateFile(dividedReport, this::class.java.getResourceAsStream("/template.docx")!!)
    }

    fun generateFile(dividedReport: DividedReport, template: InputStream): ByteArray {
        val templateDocument = XWPFDocument(template)
        val document = XWPFDocument()
        val styles = document.createStyles()
        styles.setStyles(templateDocument.style)
        dividedReport.summaries.forEach { summary ->
            document.writeSummary(summary)
        }
        return ByteArrayOutputStream().also {
            document.write(it)
        }.toByteArray()
    }

    private fun XWPFDocument.writeSummary(summary: ReportEntrySummary) {
        when (summary) {
            is ReportEntrySummary.MultiChoice -> writeMultiChoiceSummary(summary)
            is ReportEntrySummary.Text -> writeTextSummary(summary)
            is ReportEntrySummary.TextField -> writeTextFieldSummary(summary)
        }
    }

    private fun XWPFDocument.writeMultiChoiceSummary(summary: ReportEntrySummary.MultiChoice) {
        val paragraph = createParagraph()
        paragraph.createRun().apply {
            setText(summary.question)
            style = "heading 1"
        }
        createChart(summary)
    }

    private fun XWPFDocument.createChart(summary: ReportEntrySummary.MultiChoice) {
        val chart = createChart(XDDFChart.DEFAULT_WIDTH * 12, XDDFChart.DEFAULT_HEIGHT * 7)

        val numOfPoints = summary.options.size

        val categoryDataRange = chart.formatRange(CellRangeAddress(1, numOfPoints, 0, 0))
        val valuesDataRange = chart.formatRange(CellRangeAddress(1, numOfPoints, 1, 1))
        val categoriesData = XDDFDataSourcesFactory.fromArray(
            summary.options.map { it.text }.toTypedArray(),
            categoryDataRange,
            0
        )
        val valuesData = XDDFDataSourcesFactory.fromArray(
            summary.options.map { it.count }.toTypedArray(),
            valuesDataRange,
            1
        )
        valuesData.formatCode = "General"
        val pieChart = chart.createData(ChartTypes.PIE, null, null) as XDDFPieChartData
        pieChart.addSeries(categoriesData, valuesData)
        val ctChart = chart.ctChart
        val plotArea = ctChart.plotArea
        val pieChartArray = plotArea.getPieChartArray(plotArea.sizeOfPieChartArray() - 1)
        val style = pieChartArray.addNewDLbls()
        style.addNewShowPercent()
        style.addNewShowVal()
        style.addNewShowCatName().setVal(false)
        style.addNewShowSerName().setVal(false)
        val legend = chart.orAddLegend
        legend.position = LegendPosition.LEFT
        legend.isOverlay = false

        pieChart.setVaryColors(true)
        chart.plot(pieChart)
    }

    private fun XWPFDocument.writeTextSummary(summary: ReportEntrySummary.Text) {
        val paragraph = createParagraph()
        val run = paragraph.createRun()
        run.setText(summary.content)
    }

    private fun XWPFDocument.writeTextFieldSummary(summary: ReportEntrySummary.TextField) {
        val paragraph = createParagraph()
        paragraph.createRun().apply {
            setText(summary.question)
            style = "heading 1"
        }
    }
}

package com.ithersta.anketa.survey.report.exporters

import com.ithersta.anketa.survey.report.DividedReport
import com.ithersta.anketa.survey.report.ReportEntrySummary
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xddf.usermodel.chart.*
import org.apache.poi.xwpf.usermodel.XWPFChart
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
        paragraph.createRun().setText(summary.question)
        paragraph.style = "1"
        if (summary.isSingleChoice) {
            createBarChart(summary)
        } else {
            createPieChart(summary)
        }
        val description = createParagraph()
        description.createRun().setText(summary.formattedText)
    }

    private fun XDDFChartData.addSeries(chart: XWPFChart, summary: ReportEntrySummary.MultiChoice): XDDFChartData.Series {
        val numOfPoints = summary.options.size
        val categoryDataRange = chart.formatRange(CellRangeAddress(1, numOfPoints, 0, 0))
        val valuesDataRange = chart.formatRange(CellRangeAddress(1, numOfPoints, 1, 1))
        val categoriesData = XDDFDataSourcesFactory.fromArray(
            summary.options.map { it.text }.toTypedArray(),
            categoryDataRange,
            0,
        )
        val valuesData = XDDFDataSourcesFactory.fromArray(
            summary.options.map { it.count }.toTypedArray(),
            valuesDataRange,
            1,
        )
        valuesData.formatCode = "General"
        return addSeries(categoriesData, valuesData)
    }

    private fun XWPFDocument.createBarChart(summary: ReportEntrySummary.MultiChoice) {
        val chart = createChart(XDDFChart.DEFAULT_WIDTH * 12, XDDFChart.DEFAULT_HEIGHT * 7)

        val leftAxis = chart.createCategoryAxis(AxisPosition.LEFT)
        val bottomAxis = chart.createValueAxis(AxisPosition.BOTTOM)
        val barChart = chart.createData(ChartTypes.BAR, leftAxis, bottomAxis) as XDDFBarChartData
        val series = barChart.addSeries(chart, summary)
        series.setTitle(summary.question)
        barChart.setVaryColors(false)
        barChart.barDirection = BarDirection.BAR
        chart.plot(barChart)
        val ctChart = chart.ctChart
        val plotArea = ctChart.plotArea
        val barChartArray = plotArea.getBarChartArray(0)
        val style = barChartArray.addNewDLbls()
        style.addNewShowPercent()
        style.addNewShowVal()
        style.addNewShowCatName()
        style.addNewShowSerName().setVal(false)
    }

    private fun XWPFDocument.createPieChart(summary: ReportEntrySummary.MultiChoice) {
        val chart = createChart(XDDFChart.DEFAULT_WIDTH * 12, XDDFChart.DEFAULT_HEIGHT * 7)
        val pieChart = chart.createData(ChartTypes.PIE, null, null) as XDDFPieChartData
        pieChart.addSeries(chart, summary)
        val ctChart = chart.ctChart
        val plotArea = ctChart.plotArea
        val pieChartArray = plotArea.getPieChartArray(0)
        val style = pieChartArray.addNewDLbls()
        style.addNewShowPercent()
        style.addNewShowVal()
        style.addNewShowCatName()
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
        paragraph.createRun().setText(summary.question)
        paragraph.style = "1"
    }
}

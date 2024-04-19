package com.ithersta.anketa.survey.report.exporters

import com.ithersta.anketa.survey.report.DividedReport
import com.ithersta.anketa.survey.report.ReportEntrySummary
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xddf.usermodel.chart.*
import org.apache.poi.xwpf.usermodel.XWPFChart
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Component
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
            createPieChart(summary)
        } else {
            createBarChart(summary)
        }
        writeText(summary.formattedText)
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

    private fun loadBarChartTemplate(): XWPFChart {
        return this::class.java.getResourceAsStream("/bar_chart_template.docx").use { inputStream ->
            val document = XWPFDocument(inputStream)
            document.charts[0]
        }
    }

    private fun XWPFDocument.createBarChart(summary: ReportEntrySummary.MultiChoice) {
        val template = loadBarChartTemplate()
        val chart = createChart(XDDFChart.DEFAULT_WIDTH * 12, XDDFChart.DEFAULT_HEIGHT * 7)

        val leftAxis = chart.createCategoryAxis(AxisPosition.LEFT)
        val bottomAxis = chart.createValueAxis(AxisPosition.BOTTOM)
        bottomAxis.crosses = AxisCrosses.AUTO_ZERO
        bottomAxis.majorTickMark = AxisTickMark.OUT
        bottomAxis.crossBetween = AxisCrossBetween.BETWEEN
        val barChart = chart.createData(ChartTypes.BAR, leftAxis, bottomAxis) as XDDFBarChartData
        val series = barChart.addSeries(chart, summary)
        series.setTitle(summary.question)
        barChart.setVaryColors(false)
        barChart.barDirection = BarDirection.BAR
        chart.setAutoTitleDeleted(false)
        chart.plot(barChart)
        chart.setChartBottomMargin(XDDFChart.DEFAULT_HEIGHT.toLong())
        val ctChart = chart.ctChart
        val plotArea = ctChart.plotArea
        val barChartArray = plotArea.getBarChartArray(0)
        barChartArray.dLbls = template.ctChart.plotArea.getBarChartArray(0).dLbls
        barChartArray.dLbls.showPercent.setVal(true)
    }

    private fun loadPieChartTemplate(): XWPFChart {
        return this::class.java.getResourceAsStream("/pie_chart_template.docx").use { inputStream ->
            val document = XWPFDocument(inputStream)
            document.charts[0]
        }
    }

    private fun XWPFDocument.createPieChart(summary: ReportEntrySummary.MultiChoice) {
        val template = loadPieChartTemplate()
        val chart = createChart(XDDFChart.DEFAULT_WIDTH * 12, XDDFChart.DEFAULT_HEIGHT * 7)
        val pieChart = chart.createData(ChartTypes.PIE, null, null) as XDDFPieChartData
        pieChart.addSeries(chart, summary)
        val ctChart = chart.ctChart
        val plotArea = ctChart.plotArea
        val pieChartArray = plotArea.getPieChartArray(0)
        pieChartArray.dLbls = template.ctChart.plotArea.getPieChartArray(0).dLbls
        pieChartArray.dLbls.showCatName.setVal(true)
        val legend = chart.orAddLegend
        legend.position = LegendPosition.LEFT
        legend.isOverlay = false
        chart.plot(pieChart)
    }

    private fun XWPFDocument.writeTextSummary(summary: ReportEntrySummary.Text) {
        writeText(summary.content)
    }

    private fun XWPFDocument.writeTextFieldSummary(summary: ReportEntrySummary.TextField) {
        val paragraph = createParagraph()
        paragraph.createRun().setText(summary.question)
        paragraph.style = "1"
        writeText(summary.answers)
    }

    private fun XWPFDocument.writeText(text: String) {
        text.lineSequence().forEach { line ->
            val paragraph = createParagraph()
            paragraph.createRun().setText(line)
        }
    }
}

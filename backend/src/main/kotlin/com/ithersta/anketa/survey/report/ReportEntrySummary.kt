package com.ithersta.anketa.survey.report

import com.ithersta.anketa.formatting.KotlinFormatEngine
import com.ithersta.anketa.survey.domain.entries.*
import com.ithersta.anketa.survey.report.entries.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.UUID
import kotlin.math.roundToInt

sealed interface ReportEntrySummary {
    class Text(
        val content: String,
    ) : ReportEntrySummary

    class MultiChoice(
        val question: String,
        val formattedText: String,
        val options: List<Option>,
        val otherAnswers: String?,
        val answerCount: Int,
        val noAnswerCount: Int,
        val isSingleChoice: Boolean,
    ) : ReportEntrySummary {
        class Option(
            val text: String,
            val count: Int,
        )
    }

    class TextField(
        val question: String,
        val answers: String,
        val answerCount: Int,
        val noAnswerCount: Int,
    ) : ReportEntrySummary
}

suspend fun generateSummary(
    reportEntry: ReportEntry,
    surveyEntry: SurveyEntry?,
    answers: List<SurveyAnswer?>,
    getSummary: suspend (UUID) -> String?,
): ReportEntrySummary {
    return when (reportEntry) {
        is MultiChoiceReportEntry -> generateMultiChoiceReportSummary(reportEntry, surveyEntry, answers, getSummary)
        is PolarChoiceReportEntry -> generatePolarChoiceReportSummary(reportEntry, surveyEntry, answers)
        is TextFieldReportEntry -> generateTextFieldReportSummary(reportEntry, surveyEntry, answers, getSummary)
        is TextReportEntry -> generateTextReportSummary(reportEntry)
    }
}

private fun generateTextReportSummary(
    reportEntry: TextReportEntry,
): ReportEntrySummary.Text {
    return ReportEntrySummary.Text(
        content = reportEntry.content
    )
}

private suspend fun generateTextFieldReportSummary(
    reportEntry: TextFieldReportEntry,
    surveyEntry: SurveyEntry?,
    answers: List<SurveyAnswer?>,
    getSummary: suspend (UUID) -> String?,
): ReportEntrySummary.TextField {
    require(surveyEntry is TextFieldEntry) {
        "Expected survey entry of type TextField for report entry with id=${reportEntry.forEntryWithId}"
    }
    val textAnswers = mutableListOf<String>()
    var noAnswerCount = 0
    for (answer in answers) {
        if (answer == null) {
            noAnswerCount++
            continue
        }
        textAnswers.add((answer as TextFieldEntry.Answer).text)
    }
    val summary = if (reportEntry.doSummarise) getSummary(surveyEntry.id) else null
    return ReportEntrySummary.TextField(
        question = surveyEntry.question,
        answers = summary ?: textAnswers.joinToString(separator = "\n") { "• $it" },
        answerCount = answers.size,
        noAnswerCount = noAnswerCount,
    )
}

private fun generatePolarChoiceReportSummary(
    reportEntry: PolarChoiceReportEntry,
    surveyEntry: SurveyEntry?,
    answers: List<SurveyAnswer?>,
): ReportEntrySummary.MultiChoice {
    require(surveyEntry is PolarChoiceEntry) {
        "Expected survey entry of type PolarChoice for report entry with id=${reportEntry.forEntryWithId}"
    }
    val answerCounts = Array(surveyEntry.range * 2 + 1) { 0 }
    var noAnswerCount = 0
    for (answer in answers) {
        if (answer == null) {
            noAnswerCount++
            continue
        }
        check(answer is PolarChoiceEntry.Answer) {
            "Expected answer of type PolarChoice for report entry with id=${reportEntry.forEntryWithId}"
        }
        answerCounts[answer.selected + surveyEntry.range]++
    }
    val options = (-surveyEntry.range..surveyEntry.range).zip(answerCounts) { option, count ->
        ReportEntrySummary.MultiChoice.Option(
            text = option.toString(),
            count = count,
        )
    }
    val properties = generateFormattingProperties(options, answers.size, noAnswerCount)
    return ReportEntrySummary.MultiChoice(
        question = surveyEntry.question,
        formattedText = KotlinFormatEngine().format(properties, reportEntry.template),
        options = options,
        answerCount = answers.size,
        noAnswerCount = noAnswerCount,
        isSingleChoice = true,
        otherAnswers = null,
    )
}

private suspend fun generateMultiChoiceReportSummary(
    reportEntry: MultiChoiceReportEntry,
    surveyEntry: SurveyEntry?,
    answers: List<SurveyAnswer?>,
    getSummary: suspend (UUID) -> String?,
): ReportEntrySummary.MultiChoice {
    require(surveyEntry is MultiChoiceEntry) {
        "Expected survey entry of type MultiChoice for report entry with id=${reportEntry.forEntryWithId}"
    }
    val answerCounts = Array(surveyEntry.options.size) { 0 }
    var noAnswerCount = 0
    val otherAnswers = mutableListOf<String>()
    for (answer in answers) {
        if (answer == null) {
            noAnswerCount++
            continue
        }
        check(answer is MultiChoiceEntry.Answer) {
            "Expected answer of type MultiChoice for report entry with id=${reportEntry.forEntryWithId}"
        }
        answer.other?.let {
            otherAnswers.add(it)
        }
        answer.selected.forEach {
            answerCounts[it]++
        }
    }
    val summary = if (reportEntry.doSummarise) getSummary(surveyEntry.id) else null
    val formattedSummary = summary ?: otherAnswers.joinToString(separator = "\n") { "• $it" }
    val options = surveyEntry.options.zip(answerCounts) { option, count ->
        ReportEntrySummary.MultiChoice.Option(
            text = option,
            count = count,
        )
    }
    val properties = generateFormattingProperties(options, answers.size, noAnswerCount)
    return ReportEntrySummary.MultiChoice(
        question = surveyEntry.question,
        formattedText = KotlinFormatEngine().format(properties, reportEntry.template),
        otherAnswers = formattedSummary.ifBlank { null },
        options = options,
        answerCount = answers.size,
        noAnswerCount = noAnswerCount,
        isSingleChoice = surveyEntry.maxSelected == 1,
    )
}

private fun generateFormattingProperties(
    options: List<ReportEntrySummary. MultiChoice.Option>,
    answerCount: Int,
    noAnswerCount: Int,
): Map<String, Any> = buildMap {
    for ((i, option) in options.withIndex()) {
        put("c${i + 1}", option.count)
        val percent = option.count.toBigDecimal().divide(answerCount.toBigDecimal()).multiply(BigDecimal.valueOf(100)).toDouble().roundToInt()
        put("pc${i + 1}", percent)
        put("t${i + 1}", option.text)
    }
    put("ac", answerCount)
    put("nac", noAnswerCount)
}

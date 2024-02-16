package com.ithersta.anketa.survey.report

import com.ithersta.anketa.survey.domain.entries.*
import com.ithersta.anketa.survey.report.entries.*

sealed interface ReportEntrySummary {
    class Text(
        val content: String
    ) : ReportEntrySummary

    class MultiChoice(
        val question: String,
        val formattedText: String,
        val options: List<Option>,
        val answerCount: Int,
        val noAnswerCount: Int,
    ) : ReportEntrySummary {
        class Option(
            val text: String,
            val count: Int,
        )
    }

    class TextField(
        val question: String,
        val answers: List<String>,
        val answerCount: Int,
        val noAnswerCount: Int,
    ) : ReportEntrySummary
}

fun generateSummary(
    reportEntry: ReportEntry,
    surveyEntry: SurveyEntry?,
    answers: List<SurveyAnswer?>,
): ReportEntrySummary {
    return when (reportEntry) {
        is MultiChoiceReportEntry -> generateMultiChoiceReportSummary(reportEntry, surveyEntry, answers)
        is PolarChoiceReportEntry -> generatePolarChoiceReportSummary(reportEntry, surveyEntry, answers)
        is TextFieldReportEntry -> generateTextFieldReportSummary(reportEntry, surveyEntry, answers)
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

private fun generateTextFieldReportSummary(
    reportEntry: TextFieldReportEntry,
    surveyEntry: SurveyEntry?,
    answers: List<SurveyAnswer?>,
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
    }
    return ReportEntrySummary.TextField(
        question = surveyEntry.question,
        answers = textAnswers,
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
    return ReportEntrySummary.MultiChoice(
        question = surveyEntry.question,
        formattedText = reportEntry.text, // TODO: Implement
        options = options,
        answerCount = answers.size,
        noAnswerCount = noAnswerCount,
    )
}

private fun generateMultiChoiceReportSummary(
    reportEntry: MultiChoiceReportEntry,
    surveyEntry: SurveyEntry?,
    answers: List<SurveyAnswer?>,
): ReportEntrySummary.MultiChoice {
    require(surveyEntry is MultiChoiceEntry) {
        "Expected survey entry of type MultiChoice for report entry with id=${reportEntry.forEntryWithId}"
    }
    val answerCounts = Array(surveyEntry.options.size) { 0 }
    var noAnswerCount = 0
    for (answer in answers) {
        if (answer == null) {
            noAnswerCount++
            continue
        }
        check(answer is MultiChoiceEntry.Answer) {
            "Expected answer of type MultiChoice for report entry with id=${reportEntry.forEntryWithId}"
        }
        answer.selected.forEach {
            answerCounts[it]++
        }
    }
    val options = surveyEntry.options.zip(answerCounts) { option, count ->
        ReportEntrySummary.MultiChoice.Option(
            text = option,
            count = count,
        )
    }
    return ReportEntrySummary.MultiChoice(
        question = surveyEntry.question,
        formattedText = reportEntry.text, // TODO: Implement
        options = options,
        answerCount = answers.size,
        noAnswerCount = noAnswerCount,
    )
}

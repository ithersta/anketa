package com.ithersta.anketa.survey.report

import com.ithersta.anketa.survey.domain.AnswerMap
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.RequiresAnswer
import com.ithersta.anketa.survey.exporters.xlsx.toCellText

fun generateDividedReports(
    reportContent: ReportContent,
    surveyContent: SurveyContent,
    answers: List<AnswerMap>,
): List<DividedReport> {
    val surveyEntries = surveyContent.entries.associateBy { it.id }
    val divideBy = reportContent.divideBy?.let { surveyEntries[it] } as? RequiresAnswer
    val groupedAnswers = if (divideBy != null) {
        groupAnswers(divideBy, answers)
    } else {
        mapOf(surveyContent.title to answers)
    }
    return groupedAnswers.map { (name, dividedAnswers) ->
        DividedReport(
            name = name,
            summaries = reportContent.entries.map { reportEntry ->
                generateSummary(
                    reportEntry = reportEntry,
                    surveyEntry = surveyEntries[reportEntry.forEntryWithId],
                    answers = dividedAnswers.map { it.answers[reportEntry.forEntryWithId] },
                )
            }
        )
    }
}

private fun groupAnswers(
    by: RequiresAnswer,
    answers: List<AnswerMap>,
): Map<String, List<AnswerMap>> {
    return answers.groupBy { it.answers[by.id]?.toCellText(by).toString() }
}
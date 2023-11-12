package com.ithersta.anketa.survey.domain.entries

interface RequiresAnswer : SurveyEntry {
    fun isValid(answer: SurveyAnswer): Boolean
}
package com.ithersta.anketa.survey.dashboard.dto

import kotlinx.serialization.Serializable

@Serializable
class DashboardDto(
    val mySurveys: List<DashboardSurveyDto>,
    val sharedSurveys: List<DashboardSurveyDto>,
)

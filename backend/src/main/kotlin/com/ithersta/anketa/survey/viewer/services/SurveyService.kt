package com.ithersta.anketa.survey.viewer.services

import com.ithersta.anketa.survey.data.repositories.SurveyRepository
import com.ithersta.anketa.survey.data.tables.toSurveyContent
import com.ithersta.anketa.survey.domain.SurveyContent
import org.springframework.stereotype.Service
import java.util.*

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
) {
    suspend fun getContentById(id: UUID): SurveyContent? =
        surveyRepository.findById(id)?.toSurveyContent()
}
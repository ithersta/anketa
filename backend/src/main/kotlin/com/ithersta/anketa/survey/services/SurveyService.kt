package com.ithersta.anketa.survey.services

import com.ithersta.anketa.survey.data.repositories.SurveyRepository
import com.ithersta.anketa.survey.data.tables.toSurveyContent
import com.ithersta.anketa.survey.domain.SurveyContent
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
) {
    suspend fun getContentById(id: UUID): SurveyContent? =
        surveyRepository.findById(id)?.toSurveyContent()
}
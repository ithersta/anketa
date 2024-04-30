package com.ithersta.anketa.survey.viewer.services

import com.ithersta.anketa.exception.NotFoundException
import com.ithersta.anketa.survey.data.repositories.ShareRepository
import com.ithersta.anketa.survey.data.repositories.SurveyRepository
import com.ithersta.anketa.survey.data.tables.toSurveyContent
import com.ithersta.anketa.survey.domain.SurveyContent
import org.springframework.stereotype.Service
import java.util.*

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
    private val shareRepository: ShareRepository,
) {
    suspend fun getContentById(id: UUID, userId: UUID): SurveyContent =
        surveyRepository.findById(id)
            ?.takeIf { it.createdBy == userId || shareRepository.exists(id, userId) }
            ?.toSurveyContent() ?: throw NotFoundException()

    suspend fun getPublicContentById(id: UUID): SurveyContent =
        surveyRepository.findById(id)?.toSurveyContent() ?: throw NotFoundException()
}

package com.ithersta.anketa.survey.exporters.xlsx

import com.ithersta.anketa.survey.data.repositories.AnswerRepository
import com.ithersta.anketa.survey.data.tables.AnswerEntity
import com.ithersta.anketa.survey.data.tables.toAnswerMap
import com.ithersta.anketa.survey.viewer.services.SurveyService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class XlsxExportService(
    private val surveyService: SurveyService,
    private val answerRepository: AnswerRepository,
) {
    suspend fun generateXlsx(surveyId: UUID): ByteArray? {
        val surveyContent = surveyService.getContentById(surveyId) ?: return null
        val answerMaps = answerRepository.findBySurveyId(surveyId)
            .map(AnswerEntity::toAnswerMap)
        return generateXlsx(surveyContent, answerMaps)
    }
}
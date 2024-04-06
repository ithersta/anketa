package com.ithersta.anketa.survey.exporters.xlsx

import com.ithersta.anketa.exception.ForbiddenException
import com.ithersta.anketa.survey.data.repositories.AnswerRepository
import com.ithersta.anketa.survey.data.tables.AnswerEntity
import com.ithersta.anketa.survey.data.tables.toAnswerMap
import com.ithersta.anketa.survey.viewer.services.SurveyService
import org.springframework.stereotype.Service
import java.util.*

@Service
class XlsxExportService(
    private val surveyService: SurveyService,
    private val answerRepository: AnswerRepository,
) {
    suspend fun generateXlsx(surveyId: UUID, userId: UUID): ByteArray {
        val surveyContent = surveyService.getContentById(surveyId, userId) ?: throw ForbiddenException()
        val answerMaps = answerRepository.findBySurveyId(surveyId)
            .map(AnswerEntity::toAnswerMap)
        return generateXlsx(surveyContent, answerMaps)
    }
}

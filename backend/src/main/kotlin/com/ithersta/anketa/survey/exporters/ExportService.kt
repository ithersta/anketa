package com.ithersta.anketa.survey.exporters

import com.ithersta.anketa.survey.data.repositories.AnswerRepository
import com.ithersta.anketa.survey.data.tables.AnswerEntity
import com.ithersta.anketa.survey.data.tables.toAnswerMap
import com.ithersta.anketa.survey.domain.AnswerMap
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.viewer.services.SurveyService
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExportService(
    private val surveyService: SurveyService,
    private val answerRepository: AnswerRepository,
) {
    suspend fun generateXlsx(surveyId: UUID, userId: UUID): Pair<String, ByteArray> {
        return generate(surveyId, userId, ::generateXlsx)
    }

    suspend fun generateCsv(surveyId: UUID, userId: UUID): Pair<String, ByteArray> {
        return generate(surveyId, userId, ::generateCsv)
    }

    private suspend fun generate(
        surveyId: UUID,
        userId: UUID,
        format: (SurveyContent, Collection<AnswerMap>) -> ByteArray,
    ): Pair<String, ByteArray> {
        val surveyContent = surveyService.getContentById(surveyId, userId)
        val answerMaps = answerRepository.findBySurveyId(surveyId)
            .map(AnswerEntity::toAnswerMap)
        return surveyContent.title to format(surveyContent, answerMaps)
    }
}

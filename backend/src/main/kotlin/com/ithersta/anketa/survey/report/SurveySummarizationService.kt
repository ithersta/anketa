package com.ithersta.anketa.survey.report

import com.ithersta.anketa.exception.NotFoundException
import com.ithersta.anketa.gpt.SummarizationService
import com.ithersta.anketa.survey.data.repositories.AnswerRepository
import com.ithersta.anketa.survey.data.repositories.SummarizationRepository
import com.ithersta.anketa.survey.data.tables.AnswerEntity
import com.ithersta.anketa.survey.data.tables.toAnswerMap
import com.ithersta.anketa.survey.domain.entries.RequiresAnswer
import com.ithersta.anketa.survey.domain.entries.SuitableForSummarization
import com.ithersta.anketa.survey.viewer.services.SurveyService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SurveySummarizationService(
    private val surveyService: SurveyService,
    private val answerRepository: AnswerRepository,
    private val summarizationService: SummarizationService,
    private val summarizationRepository: SummarizationRepository,
) {
    suspend fun summarize(surveyId: UUID, userId: UUID, entryId: UUID): String {
        val survey = surveyService.getContentById(surveyId, userId)
        val answerMaps = answerRepository.findBySurveyId(surveyId)
            .map(AnswerEntity::toAnswerMap)
        val entry = survey.entries.find { it.id == entryId } as? RequiresAnswer ?: throw NotFoundException()
        val answers = answerMaps.mapNotNull { answerMap -> (answerMap.answers[entryId] as? SuitableForSummarization)?.contentForSummarization }
        if (answers.isEmpty()) return ""
        val summarization = summarizationService.summarize(entry.question, answers)
        summarizationRepository.upsert(entryId, summarization)
        return summarization
    }
}

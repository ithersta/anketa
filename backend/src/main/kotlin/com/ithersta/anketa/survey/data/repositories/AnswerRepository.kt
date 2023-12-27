package com.ithersta.anketa.survey.data.repositories

import com.ithersta.anketa.survey.data.tables.AnswerEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface AnswerRepository : CoroutineCrudRepository<AnswerEntity, UUID> {
    suspend fun deleteBySurveyIdAndAuthorPublicKey(surveyId: UUID, publicKey: String)
    suspend fun findBySurveyId(surveyId: UUID): List<AnswerEntity>
}
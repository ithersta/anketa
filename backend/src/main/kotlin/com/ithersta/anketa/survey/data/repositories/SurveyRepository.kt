package com.ithersta.anketa.survey.data.repositories

import com.ithersta.anketa.survey.data.tables.SurveyEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface SurveyRepository : CoroutineCrudRepository<SurveyEntity, UUID> {
    suspend fun findByCreatedBy(createdBy: UUID): List<SurveyEntity>
}
package com.ithersta.anketa.survey.data.repositories

import com.ithersta.anketa.survey.data.tables.SurveyEntity
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface SurveyRepository : CoroutineCrudRepository<SurveyEntity, UUID> {
    suspend fun findByCreatedByOrderByCreatedAtDesc(createdBy: UUID): List<SurveyEntity>
    suspend fun existsByCreatedByAndId(createdBy: UUID, id: UUID): Boolean

    @Query("""
        select * from surveys
        join shares s on surveys.id = s.survey_id
        where user_id = :userId
        order by created_at desc;
    """)
    suspend fun findShared(userId: UUID): List<SurveyEntity>
}

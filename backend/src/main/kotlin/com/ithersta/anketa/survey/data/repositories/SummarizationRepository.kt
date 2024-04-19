package com.ithersta.anketa.survey.data.repositories

import com.ithersta.anketa.survey.data.tables.SummarizationEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface SummarizationRepository : CoroutineCrudRepository<SummarizationEntity, UUID> {
    suspend fun findBySurveyEntryId(surveyEntryId: UUID): SummarizationEntity?

    @Modifying
    @Query("""
        insert into summarizations(survey_entry_id, content) 
        values (:entryId, :content)
        on conflict (survey_entry_id) do
        update set content = :content
    """)
    suspend fun upsert(entryId: UUID, content: String): SummarizationEntity
}

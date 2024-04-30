package com.ithersta.anketa.survey.data.repositories

import com.ithersta.anketa.survey.data.tables.ShareEntity
import com.ithersta.anketa.survey.data.tables.ShareEntityPrimaryKey
import com.ithersta.anketa.survey.data.tables.SurveyEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface ShareRepository : CoroutineCrudRepository<ShareEntity, ShareEntityPrimaryKey> {
    @Modifying
    @Query("""
        insert into shares (survey_id, user_id) values (:survey_id, :user_id)
        on conflict do nothing;
    """)
    suspend fun upsert(surveyId: UUID, userId: UUID)

    @Query("""
        select exists(select 1 from shares where survey_id = :surveyId and user_id = :userId)
    """)
    suspend fun exists(surveyId: UUID, userId: UUID): Boolean
}

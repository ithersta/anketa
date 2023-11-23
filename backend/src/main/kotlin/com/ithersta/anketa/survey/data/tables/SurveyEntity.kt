package com.ithersta.anketa.survey.data.tables

import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.*

@Table(name = "surveys")
class SurveyEntity(
    val title: String,
    val entries: List<SurveyEntry>,
) {
    @Column("created_by")
    @CreatedBy
    var createdBy: UUID? = null

    @Column("created_at")
    @CreatedDate
    var createdAt: Instant? = null

    @Id
    var id: UUID? = null
}

fun SurveyEntity.toSurveyContent(): SurveyContent = SurveyContent(
    title = title,
    entries = entries,
)

fun SurveyContent.toSurveyEntity(): SurveyEntity = SurveyEntity(
    title = title,
    entries = entries,
)
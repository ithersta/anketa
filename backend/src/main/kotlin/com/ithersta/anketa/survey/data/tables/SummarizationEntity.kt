package com.ithersta.anketa.survey.data.tables

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("summarizations")
class SummarizationEntity(
    @Column("survey_entry_id")
    val surveyEntryId: UUID,
    @Column("content")
    val content: String,
)

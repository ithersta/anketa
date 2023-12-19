package com.ithersta.anketa.survey.data.tables

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table(name = "answers")
class AnswerEntity(
    @Column("survey_id")
    val surveyId: UUID,
    @Column("author_public_key")
    val authorPublicKey: String,
    val entries: ByteArray,
) {
    @Id
    var id: UUID? = null
}
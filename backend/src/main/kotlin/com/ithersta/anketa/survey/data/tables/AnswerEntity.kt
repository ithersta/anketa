@file:OptIn(ExperimentalSerializationApi::class)

package com.ithersta.anketa.survey.data.tables

import com.ithersta.anketa.survey.domain.AnswerMap
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*

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

fun AnswerEntity.toAnswerMap() = AnswerMap(
    answers = ProtoBuf.decodeFromByteArray(entries)
)

fun AnswerMap.toAnswerEntity(surveyId: UUID, publicKey: String) = AnswerEntity(
    surveyId = surveyId,
    authorPublicKey = publicKey,
    entries = ProtoBuf.encodeToByteArray(answers),
)
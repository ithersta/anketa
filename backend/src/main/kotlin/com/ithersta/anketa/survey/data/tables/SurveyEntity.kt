@file:OptIn(ExperimentalSerializationApi::class)

package com.ithersta.anketa.survey.data.tables

import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
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
    val entries: ByteArray,
) {
    @Id
    var id: UUID? = null
}

fun SurveyEntity.toSurveyContent(): SurveyContent = SurveyContent(
    title = title,
    entries = ProtoBuf.decodeFromByteArray(entries),
)

fun SurveyContent.toSurveyEntity(): SurveyEntity = SurveyEntity(
    title = title,
    entries = ProtoBuf.encodeToByteArray(entries),
)
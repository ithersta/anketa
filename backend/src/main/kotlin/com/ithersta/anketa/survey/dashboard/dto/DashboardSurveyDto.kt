package com.ithersta.anketa.survey.dashboard.dto

import com.ithersta.anketa.serialization.UuidSerializer
import com.ithersta.anketa.survey.data.tables.SurveyEntity
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class DashboardSurveyDto(
    @Serializable(with = UuidSerializer::class)
    val id: UUID,
    val title: String,
    val createdAt: Long,
)

fun SurveyEntity.toDashboardSurveyDto() = DashboardSurveyDto(
    id = id!!,
    title = title,
    createdAt = createdAt.toEpochMilli(),
)

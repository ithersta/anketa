package com.ithersta.anketa.survey.data.tables

import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.util.*

@Table(name = "shares")
class ShareEntity(
    val surveyId: UUID,
    val userId: UUID,
)

class ShareEntityPrimaryKey(
    val surveyId: UUID,
    val userId: UUID,
) : Serializable

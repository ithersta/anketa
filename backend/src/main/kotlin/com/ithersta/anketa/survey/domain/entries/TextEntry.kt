package com.ithersta.anketa.survey.domain.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Text")
data class TextEntry(
    override val id: Int,
    val content: String,
) : SurveyEntry {
    override fun isValid() = content.isNotBlank()
}
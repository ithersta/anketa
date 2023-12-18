package com.ithersta.anketa.survey.domain.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("Text")
data class TextEntry(
    @Serializable(with = UuidSerializer::class)
    override var id: UUID,
    val content: String,
) : SurveyEntry {
    override fun validate() = emptyList<Nothing>()
}
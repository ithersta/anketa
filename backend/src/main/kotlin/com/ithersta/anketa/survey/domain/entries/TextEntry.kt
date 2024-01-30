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
    sealed interface ValidationError : SurveyEntry.ValidationError {
        object EmptyContent : ValidationError {
            override val message: String
                get() = "Content cannot be empty"
        }
    }

    override fun validate() = buildList {
        if (content.isEmpty()) {
            add(ValidationError.EmptyContent)
        }
    }
}
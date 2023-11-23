package com.ithersta.anketa.survey.domain.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("TextField")
data class TextFieldEntry(
    @Serializable(with = UuidSerializer::class)
    override val id: UUID,
    override val isOptional: Boolean,
    val question: String,
    val minLength: Int,
    val maxLength: Int,
) : SurveyEntry, RequiresAnswer {
    @Serializable
    @SerialName("TextField")
    data class Answer(
        val text: String,
    ) : SurveyAnswer

    sealed interface ValidationError : SurveyEntry.ValidationError {
        object MinLengthGreaterThanMaxLength : ValidationError
    }

    override fun isAnswerValid(answer: SurveyAnswer) = answer is Answer &&
            answer.text.length in minLength..maxLength

    override fun validate() = buildList {
        if (minLength > maxLength) {
            add(ValidationError.MinLengthGreaterThanMaxLength)
        }
    }
}
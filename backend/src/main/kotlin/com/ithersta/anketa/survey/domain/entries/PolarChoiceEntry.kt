package com.ithersta.anketa.survey.domain.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("PolarChoice")
data class PolarChoiceEntry(
    @Serializable(with = UuidSerializer::class)
    override val id: UUID,
    override val isRequired: Boolean,
    val question: String,
    val range: Int,
) : SurveyEntry, RequiresAnswer {
    @Serializable
    @SerialName("PolarChoice")
    data class Answer(
        val selected: Int,
    ) : SurveyAnswer

    sealed interface ValidationError : SurveyEntry.ValidationError {
        data class InvalidRange(val validRanges: IntRange) : ValidationError
    }

    override fun isAnswerValid(answer: SurveyAnswer) = answer is Answer &&
            answer.selected in (-range)..range

    override fun validate() = buildList {
        val validRanges = 1..3
        if (range !in validRanges) {
            add(ValidationError.InvalidRange(validRanges))
        }
    }
}

package com.ithersta.anketa.survey.domain.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("PolarChoice")
data class PolarChoiceEntry(
    @Serializable(with = UuidSerializer::class)
    override var id: UUID,
    override val isRequired: Boolean,
    override val question: String,
    val range: Int,
    val minText: String? = null,
    val maxText: String? = null,
) : SurveyEntry, RequiresAnswer {
    @Serializable
    @SerialName("PolarChoice")
    data class Answer(
        val selected: Int,
    ) : SurveyAnswer {
        sealed interface ValidationError : SurveyAnswer.ValidationError {
            object InvalidType : SurveyAnswer.ValidationError.InvalidType(), ValidationError
            object SelectionNotInRange : ValidationError {
                override val message: String
                    get() = "Selection isn't in the range"
            }
        }
    }

    sealed interface ValidationError : SurveyEntry.ValidationError {
        data class InvalidRange(val validRanges: IntRange) : ValidationError {
            override val message: String
                get() = "Invalid range. Valid ranges: $validRanges"
        }
    }

    override fun validateAnswer(answer: SurveyAnswer) = buildList {
        if (answer !is Answer) {
            add(Answer.ValidationError.InvalidType)
            return@buildList
        }
        if (answer.selected !in (-range)..range) {
            add(Answer.ValidationError.SelectionNotInRange)
        }
    }

    override fun validate() = buildList {
        val validRanges = 1..3
        if (range !in validRanges) {
            add(ValidationError.InvalidRange(validRanges))
        }
    }
}

package com.ithersta.anketa.survey.domain.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("MultiChoice")
data class MultiChoiceEntry @OptIn(ExperimentalSerializationApi::class) constructor(
    @Serializable(with = UuidSerializer::class)
    override var id: UUID,
    override val isRequired: Boolean,
    val question: String,
    val options: List<String>,
    val isAcceptingOther: Boolean,
    val minSelected: Int,
    val maxSelected: Int,
    @EncodeDefault
    val otherMaxLength: Int = 128,
) : SurveyEntry, RequiresAnswer {
    @Serializable
    @SerialName("MultiChoice")
    data class Answer(
        val selected: Set<Int>,
        val other: String?
    ) : SurveyAnswer, SuitableForSummarization {
        sealed interface ValidationError : SurveyAnswer.ValidationError {
            object InvalidType : SurveyAnswer.ValidationError.InvalidType(), ValidationError
            object InvalidSelection : ValidationError {
                override val message: String
                    get() = "Selected answers don't match options"
            }

            object InvalidSelectionCount : ValidationError {
                override val message: String
                    get() = "Selected answers count doesn't match the range"
            }

            object ExtraOtherAnswer : ValidationError {
                override val message: String
                    get() = "Other answer isn't accepted but is present"
            }

            object OtherMaxLengthExceeded : ValidationError {
                override val message: String
                    get() = "Max length of other answer exceeded"
            }
        }

        override val contentForSummarization: String?
            get() = other
    }

    sealed interface ValidationError : SurveyEntry.ValidationError {
        object OptionsEmpty : ValidationError {
            override val message: String
                get() = "Option list cannot be empty"
        }

        object InvalidOptionsRange : ValidationError {
            override val message: String
                get() = "Invalid selected options range"
        }
    }

    override fun validateAnswer(answer: SurveyAnswer) = buildList {
        if (answer !is Answer) {
            add(Answer.ValidationError.InvalidType)
            return@buildList
        }
        if (answer.selected.any { it !in options.indices }) {
            add(Answer.ValidationError.InvalidSelection)
        }
        val selectedCount = answer.selected.size + (if (answer.other == null) 0 else 1)
        if (selectedCount !in minSelected..maxSelected) {
            add(Answer.ValidationError.InvalidSelectionCount)
        }
        if (answer.other != null && !isAcceptingOther) {
            add(Answer.ValidationError.ExtraOtherAnswer)
        }
        if (answer.other != null && answer.other.length > otherMaxLength) {
            add(Answer.ValidationError.OtherMaxLengthExceeded)
        }
    }

    override fun validate() = buildList {
        if (options.isEmpty()) {
            add(ValidationError.OptionsEmpty)
        }
        if (minSelected !in 0..maxSelected ||
            maxSelected !in 1..options.size
        ) {
            add(ValidationError.InvalidOptionsRange)
        }
    }
}

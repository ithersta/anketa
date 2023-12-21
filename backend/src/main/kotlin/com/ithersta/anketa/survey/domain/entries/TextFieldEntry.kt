package com.ithersta.anketa.survey.domain.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("TextField")
data class TextFieldEntry(
    @Serializable(with = UuidSerializer::class)
    override var id: UUID,
    override val isRequired: Boolean,
    val question: String,
    val minLength: Int,
    val maxLength: Int,
) : SurveyEntry, RequiresAnswer {
    @Serializable
    @SerialName("TextField")
    data class Answer(
        val text: String,
    ) : SurveyAnswer {
        sealed interface ValidationError : SurveyAnswer.ValidationError {
            object InvalidType : SurveyAnswer.ValidationError.InvalidType(), ValidationError
            object TextLengthNotInRange : ValidationError {
                override val message: String
                    get() = "Text length isn't in the range"
            }
        }
    }

    sealed interface ValidationError : SurveyEntry.ValidationError {
        object MinLengthGreaterThanMaxLength : ValidationError {
            override val message: String
                get() = "Min length cannot be larger than max length"
        }
    }

    override fun validateAnswer(answer: SurveyAnswer) = buildList {
        if (answer !is Answer) {
            add(Answer.ValidationError.InvalidType)
            return@buildList
        }
        if (answer.text.length !in minLength..maxLength) {
            add(Answer.ValidationError.TextLengthNotInRange)
        }
    }

    override fun validate() = buildList {
        if (minLength > maxLength) {
            add(ValidationError.MinLengthGreaterThanMaxLength)
        }
    }
}
package com.ithersta.anketa.survey.domain.entries

import com.ithersta.anketa.serialization.UuidSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SerialName("MultiChoice")
data class MultiChoiceEntry(
    @Serializable(with = UuidSerializer::class)
    override var id: UUID,
    override val isRequired: Boolean,
    val question: String,
    val options: List<String>,
    val isAcceptingOther: Boolean,
    val minSelected: Int,
    val maxSelected: Int,
) : SurveyEntry, RequiresAnswer {
    @Serializable
    @SerialName("MultiChoice")
    data class Answer(
        val selectedIds: Set<Int>,
        val other: String?
    ) : SurveyAnswer

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

    override fun isAnswerValid(answer: SurveyAnswer) = answer is Answer &&
            answer.selectedIds.all { it in options.indices } &&
            answer.selectedIds.size + (if (answer.other == null) 0 else 1) in minSelected..maxSelected &&
            (answer.other == null || isAcceptingOther)

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

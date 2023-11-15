package com.ithersta.anketa.survey.domain.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("MultiChoice")
data class MultiChoiceEntry(
    override val id: Int,
    val question: String,
    val minSelected: Int,
    val maxSelected: Int,
    val options: List<String>,
) : SurveyEntry, RequiresAnswer {
    @Serializable
    @SerialName("MultiChoice")
    data class Answer(
        val selectedIds: Set<Int>,
    ) : SurveyAnswer

    override fun isValid(answer: SurveyAnswer) = answer is Answer &&
            answer.selectedIds.all { it in options.indices } &&
            answer.selectedIds.size in minSelected..maxSelected

    override fun isValid() = options.isNotEmpty() &&
            minSelected in 0..maxSelected &&
            maxSelected in 1..options.size
}

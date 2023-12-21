package com.ithersta.anketa.survey.domain

import arrow.core.NonEmptyList
import arrow.core.toNonEmptyListOrNull
import com.ithersta.anketa.serialization.UuidSerializer
import com.ithersta.anketa.survey.domain.entries.RequiresAnswer
import com.ithersta.anketa.survey.domain.entries.SurveyAnswer
import kotlinx.serialization.Serializable
import java.util.*

@JvmInline
@Serializable
value class AnswerMap(
    val answers: Map<@Serializable(with = UuidSerializer::class) UUID, SurveyAnswer>
) {
    sealed interface ValidationError {
        val message: String

        object ExtraAnswers : ValidationError {
            override val message: String
                get() = "Answer map cannot contain extra answers"
        }

        data class MissingAnswers(
            val ids: Set<UUID>,
        ) : ValidationError {
            override val message: String
                get() = "Missing answers for ids:\n${ids.joinToString(separator = "\n")}"
        }

        data class InvalidAnswer(
            val id: UUID,
            val errors: NonEmptyList<SurveyAnswer.ValidationError>,
        ) : ValidationError {
            override val message: String
                get() = "id=$id:\n${errors.joinToString(separator = "\n") { it.message }}"
        }
    }
}

fun SurveyContent.validateAnswers(answerMap: AnswerMap): List<AnswerMap.ValidationError> = buildList {
    val answers = answerMap.answers
    val requiresAnswers = entries.filterIsInstance<RequiresAnswer>()

    if (!requiresAnswers.map { it.id }.containsAll(answers.keys)) {
        add(AnswerMap.ValidationError.ExtraAnswers)
        return@buildList
    }

    val missingAnswerIds = mutableSetOf<UUID>()

    for (entry in requiresAnswers) {
        val answer = answers[entry.id]
        if (answer == null) {
            if (entry.isRequired) {
                missingAnswerIds.add(entry.id)
            }
        } else {
            val errors = entry.validateAnswer(answer).toNonEmptyListOrNull()
            if (errors != null) {
                add(AnswerMap.ValidationError.InvalidAnswer(entry.id, errors))
            }
        }
    }

    if (missingAnswerIds.isNotEmpty()) {
        add(AnswerMap.ValidationError.MissingAnswers(missingAnswerIds))
    }
}
package com.ithersta.anketa.survey.domain

import arrow.core.NonEmptyList
import arrow.core.toNonEmptyListOrNull
import com.ithersta.anketa.survey.domain.entries.RequiresAnswer
import com.ithersta.anketa.survey.domain.entries.SurveyAnswer
import java.util.*
import kotlin.collections.HashMap

sealed interface AnswerMapValidationError {
    val message: String

    object ExtraAnswers : AnswerMapValidationError {
        override val message: String
            get() = "Answer map cannot contain extra answers"
    }

    data class MissingAnswers(
        val ids: Set<UUID>,
    ) : AnswerMapValidationError {
        override val message: String
            get() = "Missing answers for ids:\n${ids.joinToString(separator = "\n")}"
    }

    data class InvalidAnswer(
        val id: UUID,
        val errors: NonEmptyList<SurveyAnswer.ValidationError>,
    ) : AnswerMapValidationError {
        override val message: String
            get() = "id=$id:\n${errors.joinToString(separator = "\n") { it.message }}"
    }
}

fun SurveyContent.validateAnswers(answers: Map<UUID, SurveyAnswer>): List<AnswerMapValidationError> = buildList {
    val requiresAnswers = entries.filterIsInstance<RequiresAnswer>()

    val extraAnswerIds = answers.keys - requiresAnswers.map { it.id }.toSet()
    if (extraAnswerIds.isNotEmpty()) {
        add(AnswerMapValidationError.ExtraAnswers)
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
                add(AnswerMapValidationError.InvalidAnswer(entry.id, errors))
            }
        }
    }

    if (missingAnswerIds.isNotEmpty()) {
        add(AnswerMapValidationError.MissingAnswers(missingAnswerIds))
    }
}
package com.ithersta.anketa.survey.domain

import com.ithersta.anketa.survey.domain.entries.RequiresAnswer
import com.ithersta.anketa.survey.domain.entries.SurveyAnswer
import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SurveyContent(
    val title: String,
    val entries: List<SurveyEntry>,
) {
    sealed interface ValidationError {
        val message: String

        object Empty : ValidationError {
            override val message: String
                get() = "Survey cannot be empty"
        }
        object DuplicateId : ValidationError {
            override val message: String
                get() = "Survey cannot contain entries with duplicate ids"
        }
        data class InvalidEntry(
            val id: UUID,
            val errors: List<SurveyEntry.ValidationError>
        ) : ValidationError {
            override val message: String
                get() = "id=$id: ${errors.joinToString(separator = "\n") { it.message }}"
        }
    }
}

fun SurveyContent.validate(): List<SurveyContent.ValidationError> = buildList {
    if (entries.isEmpty()) {
        add(SurveyContent.ValidationError.Empty)
    }
    if (entries.asSequence().distinctBy { it.id }.count() != entries.size) {
        add(SurveyContent.ValidationError.DuplicateId)
    }
    entries.forEach { entry ->
        val errors = entry.validate()
        if (errors.isNotEmpty()) {
            add(SurveyContent.ValidationError.InvalidEntry(entry.id, errors))
        }
    }
}

fun SurveyContent.isAnswerValid(answers: Map<UUID, SurveyAnswer>): Boolean =
    entries.asSequence()
        .filterIsInstance<RequiresAnswer>()
        .all { entry ->
            val answer = answers[entry.id]
            if (answer == null) {
                !entry.isRequired
            } else {
                entry.isAnswerValid(answer)
            }
        }

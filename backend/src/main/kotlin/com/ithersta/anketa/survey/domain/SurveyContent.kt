package com.ithersta.anketa.survey.domain

import arrow.core.NonEmptyList
import arrow.core.toNonEmptyListOrNull
import arrow.core.toNonEmptySetOrNull
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
            val errors: NonEmptyList<SurveyEntry.ValidationError>
        ) : ValidationError {
            override val message: String
                get() = "id=$id:\n${errors.joinToString(separator = "\n") { it.message }}"
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
        val errors = entry.validate().toNonEmptyListOrNull()
        if (errors != null) {
            add(SurveyContent.ValidationError.InvalidEntry(entry.id, errors))
        }
    }
}


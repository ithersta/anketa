package com.ithersta.anketa.survey.exporters

import com.ithersta.anketa.survey.domain.entries.*

fun SurveyAnswer.toCellText(entry: RequiresAnswer) = when (this) {
    is MultiChoiceEntry.Answer -> {
        check(entry is MultiChoiceEntry)
        sequence {
            yieldAll(selected.asSequence().map { entry.options[it] })
            if (other != null) yield(other)
        }.joinToString(separator = "\r\n")
    }

    is PolarChoiceEntry.Answer -> selected.toString()
    is TextFieldEntry.Answer -> text
}

fun RequiresAnswer.toCellText() = when (this) {
    is MultiChoiceEntry -> question
    is PolarChoiceEntry -> question
    is TextFieldEntry -> question
}

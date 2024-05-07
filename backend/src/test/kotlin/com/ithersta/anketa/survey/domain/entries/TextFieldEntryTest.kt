package com.ithersta.anketa.survey.domain.entries

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be in`
import org.junit.jupiter.api.Test
import java.util.*

class TextFieldEntryTest {
    private val entry = TextFieldEntry(
        id = UUID(0, 1),
        isRequired = true,
        question = "Question",
        minLength = 2,
        maxLength = 4,
    )

    @Test
    fun `given valid entry when validate then no error is returned`() {
        entry.validate().size `should be equal to` 0
    }

    @Test
    fun `given maxLength smaller than minLength when validate then MinLengthGreaterThanMaxLength is returned`() {
        TextFieldEntry.ValidationError.MinLengthGreaterThanMaxLength `should be in`
                entry.copy(minLength = 2, maxLength = 1).validate()
    }

    @Test
    fun `given invalid minLength when validate then InvalidLength is returned`() {
        TextFieldEntry.ValidationError.InvalidLength `should be in`
                entry.copy(minLength = -1).validate()
    }
    @Test
    fun `given valid answer when validateAnswer then no error is returned`() {
        entry.validateAnswer(TextFieldEntry.Answer("fors")).size `should be equal to` 0
    }

    @Test
    fun `given invalid type answer when validateAnswer then InvalidType is returned`() {
        TextFieldEntry.Answer.ValidationError.InvalidType `should be in`
                entry.validateAnswer(PolarChoiceEntry.Answer(0))
    }

    @Test
    fun `given too long answer when validateAnswer then TextLengthNotInRange is returned`() {
        TextFieldEntry.Answer.ValidationError.TextLengthNotInRange `should be in`
                entry.validateAnswer(TextFieldEntry.Answer("forse"))
    }

    @Test
    fun `given too little answer when validateAnswer then TextLengthNotInRange is returned`() {
        TextFieldEntry.Answer.ValidationError.TextLengthNotInRange `should be in`
                entry.validateAnswer(TextFieldEntry.Answer("f"))
    }
}

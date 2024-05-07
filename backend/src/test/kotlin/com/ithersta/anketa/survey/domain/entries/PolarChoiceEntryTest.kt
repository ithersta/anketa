package com.ithersta.anketa.survey.domain.entries

import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be in`
import org.junit.jupiter.api.Test
import java.util.*

class PolarChoiceEntryTest {
    private val entry = PolarChoiceEntry(
        id = UUID(0, 1),
        isRequired = true,
        question = "Question",
        range = 2,
    )

    @Test
    fun `given valid entry when validate then no error is returned`() {
        entry.validate().size `should be equal to` 0
    }

    @Test
    fun `given 0 range when validate then InvalidRange is returned`() {
        PolarChoiceEntry.ValidationError.InvalidRange(1..3) `should be in`
                entry.copy(range = 0).validate()
    }

    @Test
    fun `given range bigger than 3 when validate then InvalidRange is returned`() {
        PolarChoiceEntry.ValidationError.InvalidRange(1..3) `should be in`
                entry.copy(range = 4).validate()
    }

    @Test
    fun `given valid answer when validateAnswer then no error is returned`() {
        entry.validateAnswer(PolarChoiceEntry.Answer(0)).size `should be equal to` 0
    }

    @Test
    fun `given invalid answer type when validateAnswer then InvalidType is returned`() {
        PolarChoiceEntry.Answer.ValidationError.InvalidType `should be in`
                entry.validateAnswer(TextFieldEntry.Answer("Answer"))
    }

    @Test
    fun `given answer bigger than max when validateAnswer then SelectionNotInRange is returned`() {
        PolarChoiceEntry.Answer.ValidationError.SelectionNotInRange `should be in`
                entry.validateAnswer(PolarChoiceEntry.Answer(3))
    }

    @Test
    fun `given answer smaller than min when validateAnswer then SelectionNotInRange is returned`() {
        PolarChoiceEntry.Answer.ValidationError.SelectionNotInRange `should be in`
                entry.validateAnswer(PolarChoiceEntry.Answer(-3))
    }
}

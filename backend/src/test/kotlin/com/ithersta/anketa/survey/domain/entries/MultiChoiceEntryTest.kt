package com.ithersta.anketa.survey.domain.entries

import com.ithersta.anketa.survey.domain.entries.MultiChoiceEntry.Answer
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be in`
import org.junit.jupiter.api.Test
import java.util.*

class MultiChoiceEntryTest {
    private val notAcceptingOtherEntry = MultiChoiceEntry(
        id = UUID(0, 0),
        isRequired = true,
        question = "Question",
        options = listOf(
            "Option 1",
            "Option 2",
            "Option 3",
        ),
        isAcceptingOther = false,
        minSelected = 1,
        maxSelected = 2,
    )
    private val acceptingOtherEntry = notAcceptingOtherEntry
        .copy(isAcceptingOther = true, otherMaxLength = 8)

    @Test
    fun `given correct entry when validate then no error is returned`() {
        val errors = notAcceptingOtherEntry.validate()
        errors.size `should be equal to` 0
    }

    @Test
    fun `given empty options when validate then OptionsEmpty is returned`() {
        MultiChoiceEntry.ValidationError.OptionsEmpty `should be in`
                notAcceptingOtherEntry.copy(options = emptyList()).validate()
    }

    @Test
    fun `given minSelected bigger than options size when validate then InvalidOptionsRange is returned`() {
        MultiChoiceEntry.ValidationError.InvalidOptionsRange `should be in`
                notAcceptingOtherEntry.copy(minSelected = 4).validate()
    }

    @Test
    fun `given maxSelected bigger than options size when validate then InvalidOptionsRange is returned`() {
        MultiChoiceEntry.ValidationError.InvalidOptionsRange `should be in`
                notAcceptingOtherEntry.copy(maxSelected = 5).validate()
    }

    @Test
    fun `given minSelected smaller than 0 when validate then InvalidOptionsRange is returned`() {
        MultiChoiceEntry.ValidationError.InvalidOptionsRange `should be in`
                notAcceptingOtherEntry.copy(minSelected = -1).validate()
    }

    @Test
    fun `given maxSelected smaller than minSelected when validate then InvalidOptionsRange is returned`() {
        MultiChoiceEntry.ValidationError.InvalidOptionsRange `should be in`
                notAcceptingOtherEntry.copy(maxSelected = 2, minSelected = 3).validate()
    }

    @Test
    fun `given maxSelected smaller than 1 when validate then InvalidOptionsRange is returned`() {
        MultiChoiceEntry.ValidationError.InvalidOptionsRange `should be in`
                notAcceptingOtherEntry.copy(maxSelected = 0, minSelected = 0).validate()
    }

    @Test
    fun `given correct answer and not accepting other when validateAnswer then no error is returned`() {
        val errors = notAcceptingOtherEntry.validateAnswer(Answer(setOf(1), null))
        errors.size `should be equal to` 0
    }

    @Test
    fun `given correct answer and accepting other when validateAnswer then no error is returned`() {
        val errors = acceptingOtherEntry.validateAnswer(Answer(setOf(1), "Other"))
        errors.size `should be equal to` 0
    }

    @Test
    fun `given invalid type when validateAnswer then InvalidType is returned`() {
        val errors = notAcceptingOtherEntry.validateAnswer(TextFieldEntry.Answer("text"))
        Answer.ValidationError.InvalidType `should be in` errors
    }

    @Test
    fun `given selection not present when validateAnswer then InvalidSelection is returned`() {
        val errors = notAcceptingOtherEntry.validateAnswer(Answer(setOf(3), null))
        Answer.ValidationError.InvalidSelection `should be in` errors
    }

    @Test
    fun `given negative selection when validateAnswer then InvalidSelection is returned`() {
        val errors = notAcceptingOtherEntry.validateAnswer(Answer(setOf(-1), null))
        Answer.ValidationError.InvalidSelection `should be in` errors
    }

    @Test
    fun `given too many selections when validateAnswer then InvalidSelectionCount is returned`() {
        val errors = notAcceptingOtherEntry.validateAnswer(Answer(setOf(0, 1, 2), null))
        Answer.ValidationError.InvalidSelectionCount `should be in` errors
    }

    @Test
    fun `given too little selections when validateAnswer then InvalidSelectionCount is returned`() {
        val errors = notAcceptingOtherEntry.validateAnswer(Answer(setOf(), null))
        Answer.ValidationError.InvalidSelectionCount `should be in` errors
    }

    @Test
    fun `given other answer and it is not accepted when validateAnswer then ExtraOtherAnswer is returned`() {
        val errors = notAcceptingOtherEntry.validateAnswer(Answer(setOf(1), "Other"))
        Answer.ValidationError.ExtraOtherAnswer `should be in` errors
    }

    @Test
    fun `given other length is greater than max when validateAnswer then OtherMaxLengthExceeded is returned`() {
        val errors = acceptingOtherEntry.validateAnswer(Answer(setOf(1), "TooLongOtherAnswer"))
        Answer.ValidationError.OtherMaxLengthExceeded `should be in` errors
    }
}

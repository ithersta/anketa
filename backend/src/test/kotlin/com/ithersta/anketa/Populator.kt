package com.ithersta.anketa

import com.ithersta.anketa.auth.data.repositories.UserRepository
import com.ithersta.anketa.auth.data.tables.UserEntity
import com.ithersta.anketa.auth.services.JwtService
import com.ithersta.anketa.survey.data.repositories.AnswerRepository
import com.ithersta.anketa.survey.data.repositories.SurveyRepository
import com.ithersta.anketa.survey.data.tables.toAnswerEntity
import com.ithersta.anketa.survey.data.tables.toSurveyEntity
import com.ithersta.anketa.survey.domain.AnswerMap
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.MultiChoiceEntry
import com.ithersta.anketa.survey.domain.entries.PolarChoiceEntry
import com.ithersta.anketa.survey.domain.entries.TextEntry
import com.ithersta.anketa.survey.domain.entries.TextFieldEntry
import com.ithersta.anketa.survey.report.ReportContent
import com.ithersta.anketa.survey.report.entries.MultiChoiceReportEntry
import com.ithersta.anketa.survey.report.entries.PolarChoiceReportEntry
import com.ithersta.anketa.survey.report.entries.TextFieldReportEntry
import com.ithersta.anketa.survey.report.entries.TextReportEntry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class Populator(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val surveyRepository: SurveyRepository,
    private val answerRepository: AnswerRepository,
) {
    lateinit var token: String
    lateinit var userId: UUID
    lateinit var surveyId: UUID
    private val multiChoiceEntry = MultiChoiceEntry(
        id = UUID(0, 1),
        isRequired = true,
        question = "Question",
        options = listOf(
            "Option 1",
            "Option 2",
        ),
        isAcceptingOther = false,
        minSelected = 1,
        maxSelected = 2,
    )
    private val polarChoiceEntry = PolarChoiceEntry(
        id = UUID(0, 2),
        isRequired = true,
        question = "Question",
        range = 2,
    )
    private val textFieldEntry = TextFieldEntry(
        id = UUID(0, 3),
        isRequired = true,
        question = "Question",
        minLength = 10,
        maxLength = 20,
    )
    private val textEntry = TextEntry(
        id = UUID(0, 4),
        content = "Content",
    )
    val sampleSurveyContent = SurveyContent(
        title = "Big Survey",
        entries = listOf(
            multiChoiceEntry,
            polarChoiceEntry,
            textFieldEntry,
            textEntry,
        )
    )
    val sampleAnswer = AnswerMap(mapOf(
        UUID(0, 1) to MultiChoiceEntry.Answer(selected = setOf(1), other = null),
        UUID(0, 2) to PolarChoiceEntry.Answer(selected = 0),
        UUID(0, 3) to TextFieldEntry.Answer("This is the answer"),
    ))
    val sampleReport = ReportContent(
        entries = listOf(
            MultiChoiceReportEntry(forEntryWithId = UUID(0, 1), "Template", false),
            PolarChoiceReportEntry(forEntryWithId = UUID(0, 2), "Template"),
            TextFieldReportEntry(forEntryWithId = UUID(0, 3), false),
            TextReportEntry("Content"),
        ),
        divideBy = UUID(0, 2),
    )
    private val lock = Mutex()

    fun populate() {
        if (!lock.tryLock()) return
        runBlocking {
            populateUsers()
            populateSurvey()
            populateAnswers()
        }
    }

    private suspend fun populateUsers() {
        val user = userRepository.save(UserEntity("Display Name", "e@yandex.ru"))
        userId = user.id!!
        token = jwtService.generateToken(user.id!!, user.email)
    }

    private suspend fun populateSurvey() {
        surveyId = surveyRepository.save(sampleSurveyContent.toSurveyEntity(userId, Instant.now())).id!!
    }

    private suspend fun populateAnswers() {
        answerRepository.save(sampleAnswer.toAnswerEntity(surveyId, "Sample"))
    }
}

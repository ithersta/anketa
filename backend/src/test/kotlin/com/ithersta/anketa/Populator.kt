package com.ithersta.anketa

import com.ithersta.anketa.auth.data.repositories.UserRepository
import com.ithersta.anketa.auth.data.tables.UserEntity
import com.ithersta.anketa.auth.services.JwtService
import com.ithersta.anketa.survey.dashboard.services.DashboardSurveyService
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.entries.MultiChoiceEntry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import org.springframework.stereotype.Component
import java.util.*

@Component
class Populator(
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val dashboardSurveyService: DashboardSurveyService,
) {
    lateinit var token: String
    lateinit var userId: UUID
    lateinit var surveyId: UUID
    val sampleSurveyContent = SurveyContent(
        title = "Survey",
        entries = listOf(
            MultiChoiceEntry(
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
        )
    )
    private val lock = Mutex()

    fun populate() {
        if (!lock.tryLock()) return
        runBlocking {
            populateUsers()
            populateSurvey()
        }
    }

    private suspend fun populateUsers() {
        val user = userRepository.save(UserEntity("Display Name", "email@yandex.ru"))
        userId = user.id!!
        token = jwtService.generateToken(user.id!!, user.email)
    }

    private suspend fun populateSurvey() {
        dashboardSurveyService.add(
            sampleSurveyContent,
            userId,
        ).onRight { id ->
            surveyId = id!!
        }
    }
}

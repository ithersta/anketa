package com.ithersta.anketa.survey.viewer.services

import arrow.core.NonEmptyList
import arrow.core.toNonEmptyListOrNull
import com.ithersta.anketa.crypto.SignedMessage
import com.ithersta.anketa.survey.data.repositories.AnswerRepository
import com.ithersta.anketa.survey.data.tables.toAnswerEntity
import com.ithersta.anketa.survey.domain.AnswerMap
import com.ithersta.anketa.survey.domain.validateAnswers
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AnswerService(
    private val answerRepository: AnswerRepository,
    private val surveyService: SurveyService,
) {
    sealed interface AddResult {
        object OK : AddResult
        object InvalidSignature : AddResult
        object InvalidSurveyId : AddResult
        data class InvalidAnswers(
            val errors: NonEmptyList<AnswerMap.ValidationError>
        ) : AddResult
    }

    @Transactional
    suspend fun add(surveyId: UUID, signedAnswers: SignedMessage): AddResult {
        val answersString = signedAnswers.getVerifiedContent() ?: return AddResult.InvalidSignature
        val answerMap: AnswerMap = Json.decodeFromString(answersString)
        val surveyContent = surveyService.getContentById(surveyId) ?: return AddResult.InvalidSurveyId
        val errors = surveyContent.validateAnswers(answerMap).toNonEmptyListOrNull()
        if (errors != null) {
            return AddResult.InvalidAnswers(errors)
        }
        answerRepository.deleteBySurveyIdAndAuthorPublicKey(surveyId, signedAnswers.publicKey)
        answerRepository.save(answerMap.toAnswerEntity(surveyId, signedAnswers.publicKey))
        return AddResult.OK
    }
}

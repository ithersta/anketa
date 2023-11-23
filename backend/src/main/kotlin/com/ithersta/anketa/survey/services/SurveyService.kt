package com.ithersta.anketa.survey.services

import arrow.core.Either
import arrow.core.EitherNel
import arrow.core.toNonEmptyListOrNull
import com.ithersta.anketa.survey.data.repositories.SurveyRepository
import com.ithersta.anketa.survey.data.tables.toSurveyContent
import com.ithersta.anketa.survey.data.tables.toSurveyEntity
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.validate
import org.springframework.stereotype.Service
import java.util.*

@Service
class SurveyService(
    private val surveyRepository: SurveyRepository,
) {
    suspend fun getContentById(id: UUID): SurveyContent? =
        surveyRepository.findById(id)?.toSurveyContent()

    suspend fun add(content: SurveyContent): EitherNel<SurveyContent.ValidationError, UUID?> {
        val errors = content.validate().toNonEmptyListOrNull()
        return if (errors == null) {
            Either.Right(surveyRepository.save(content.toSurveyEntity()).id)
        } else {
            Either.Left(errors)
        }
    }
}
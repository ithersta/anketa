package com.ithersta.anketa.survey.dashboard.services

import arrow.core.Either
import arrow.core.EitherNel
import arrow.core.toNonEmptyListOrNull
import com.ithersta.anketa.auth.data.repositories.UserRepository
import com.ithersta.anketa.exception.NotFoundException
import com.ithersta.anketa.survey.dashboard.dto.DashboardSurveyDto
import com.ithersta.anketa.survey.dashboard.dto.toDashboardSurveyDto
import com.ithersta.anketa.survey.data.repositories.ShareRepository
import com.ithersta.anketa.survey.data.repositories.SurveyRepository
import com.ithersta.anketa.survey.data.tables.SurveyEntity
import com.ithersta.anketa.survey.data.tables.toSurveyEntity
import com.ithersta.anketa.survey.domain.SurveyContent
import com.ithersta.anketa.survey.domain.validate
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class DashboardSurveyService(
    private val surveyRepository: SurveyRepository,
    private val shareRepository: ShareRepository,
    private val userRepository: UserRepository,
) {
    suspend fun add(content: SurveyContent, createdBy: UUID): EitherNel<SurveyContent.ValidationError, UUID?> {
        content.entries.forEach { it.id = UUID.randomUUID() }
        val errors = content.validate().toNonEmptyListOrNull()
        return if (errors == null) {
            Either.Right(surveyRepository.save(content.toSurveyEntity(createdBy, Instant.now())).id)
        } else {
            Either.Left(errors)
        }
    }

    suspend fun addShare(id: UUID, email: String, authorizedUserId: UUID) {
        val isAuthorized = surveyRepository.existsByCreatedByAndId(authorizedUserId, id)
        if (!isAuthorized) {
            throw NotFoundException()
        }
        val userId = userRepository.findByEmailIgnoreCase(email)?.id ?: throw NotFoundException()
        if (userId == authorizedUserId) throw NotFoundException()
        shareRepository.upsert(id, userId)
    }

    suspend fun getAll(createdBy: UUID): List<DashboardSurveyDto> =
        surveyRepository.findByCreatedByOrderByCreatedAtDesc(createdBy).map(SurveyEntity::toDashboardSurveyDto)

    suspend fun getShared(userId: UUID): List<DashboardSurveyDto> =
        surveyRepository.findShared(userId).map(SurveyEntity::toDashboardSurveyDto)
}

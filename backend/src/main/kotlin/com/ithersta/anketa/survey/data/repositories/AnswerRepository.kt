package com.ithersta.anketa.survey.data.repositories

import com.ithersta.anketa.survey.data.tables.AnswerEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface AnswerRepository : CoroutineCrudRepository<AnswerEntity, UUID>
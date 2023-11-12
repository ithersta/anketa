package com.ithersta.anketa.survey.data.tables

import com.ithersta.anketa.survey.data.converters.SurveyEntryListConverter
import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import java.util.*

@Entity
@Table(name = "surveys")
class SurveyEntity(
    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    @Convert(converter = SurveyEntryListConverter::class)
    val entries: List<SurveyEntry>,
) {
    @Column(name = "created_by", nullable = false)
    @CreatedBy
    var createdBy: UUID? = null

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    var createdAt: Instant? = null

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
}
package com.ithersta.anketa.survey.data.converters

import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import jakarta.persistence.AttributeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object SurveyEntryListConverter : AttributeConverter<List<SurveyEntry>, String> {
    override fun convertToDatabaseColumn(attribute: List<SurveyEntry>): String =
        Json.encodeToString(attribute)

    override fun convertToEntityAttribute(dbData: String): List<SurveyEntry> =
        Json.decodeFromString(dbData)
}
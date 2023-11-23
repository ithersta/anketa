@file:OptIn(ExperimentalSerializationApi::class)

package com.ithersta.anketa.survey.data.converters

import com.ithersta.anketa.survey.domain.entries.SurveyEntry
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter

@ReadingConverter
object SurveyEntryListReadingConverter : Converter<List<SurveyEntry>, ByteArray> {
    override fun convert(source: List<SurveyEntry>): ByteArray =
        ProtoBuf.encodeToByteArray(source)
}

@WritingConverter
object SurveyEntryListWritingConverter : Converter<ByteArray, List<SurveyEntry>> {
    override fun convert(source: ByteArray): List<SurveyEntry> =
        ProtoBuf.decodeFromByteArray(source)
}

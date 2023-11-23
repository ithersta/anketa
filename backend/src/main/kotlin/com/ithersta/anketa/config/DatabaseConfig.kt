package com.ithersta.anketa.config

import com.ithersta.anketa.survey.data.converters.SurveyEntryListReadingConverter
import com.ithersta.anketa.survey.data.converters.SurveyEntryListWritingConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect

@Configuration
class DatabaseConfig {
    @Bean
    fun customConversions(): R2dbcCustomConversions = R2dbcCustomConversions.of(
        PostgresDialect.INSTANCE,
        listOf(SurveyEntryListReadingConverter, SurveyEntryListWritingConverter)
    )
}
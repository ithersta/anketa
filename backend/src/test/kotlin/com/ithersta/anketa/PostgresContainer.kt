package com.ithersta.anketa

import com.ithersta.anketa.DatabaseTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ContextConfiguration(initializers = [DatabaseTest.Initializer::class])
abstract class DatabaseTest {
    companion object {
        @Container
        val database = PostgreSQLContainer<Nothing>("postgres:latest")
    }

    object Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.r2dbc.url=${database.jdbcUrl.replace("jdbc", "r2dbc")}",
                "spring.flyway.url=${database.jdbcUrl}",
            )
        }
    }
}

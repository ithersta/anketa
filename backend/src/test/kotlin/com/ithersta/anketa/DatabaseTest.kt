package com.ithersta.anketa

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@ContextConfiguration(initializers = [DatabaseTest.Initializer::class])
abstract class DatabaseTest {
    @Autowired
    lateinit var populator: Populator

    @BeforeEach
    fun populate() {
        populator.populate()
    }

    companion object {
        val database = PostgreSQLContainer<Nothing>("postgres:16.2").also {
            it.start()
        }
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

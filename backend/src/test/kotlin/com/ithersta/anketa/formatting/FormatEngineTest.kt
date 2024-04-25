package com.ithersta.anketa.formatting

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FormatEngineTest {
    private val engine = FormatEngine

    @Test
    fun `given valid template when format then correct result is returned`() {
        val actual = engine.format(
            properties = mapOf("a" to 1, "b" to 2, "string" to "wow"),
            templateString = "a + b = \${a + b}, here's my \${string}",
        )
        assertEquals("a + b = 3, here's my wow", actual)
    }
}

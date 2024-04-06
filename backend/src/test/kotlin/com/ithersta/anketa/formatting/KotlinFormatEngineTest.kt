package com.ithersta.anketa.formatting

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class KotlinFormatEngineTest {
    private val engine = KotlinFormatEngine()

    @Test
    fun `given valid template when format then correct result is returned`() {
        val actual = engine.format(
            properties = mapOf("a" to 1, "b" to 2, "string" to "wow"),
            template = "a + b = \${a + b}, here's my \$string",
        )
        assertEquals("a + b = 3, here's my wow", actual)
    }

    @Test
    fun `given complex template when format then correct result is returned`() {
        val actual = engine.format(
            properties = mapOf("a" to 1, "b" to 2, "string" to "wow"),
            template = """
                var c = 0
                for (i in 0..10) {
                    c = i * i
                }
                "result ${'$'}c"
            """.trimIndent(),
        )
        assertEquals("result 100", actual)
    }

    @Test
    fun `given non-string template when format then exception is thrown`() {
        assertThrows<Exception> {
            engine.format(
                properties = mapOf("a" to 1, "b" to 2, "string" to "wow"),
                template = "5 to \"wow\"",
            )
        }
    }

    @Test
    fun `given invalid template when format then exception is thrown`() {
        assertThrows<Exception> {
            engine.format(
                properties = mapOf("a" to 1, "b" to 2, "string" to "wow"),
                template = "invalid\"\"",
            )
        }
    }
}

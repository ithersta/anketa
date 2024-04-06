package com.ithersta.anketa.formatting

import javax.script.ScriptContext
import javax.script.ScriptEngineManager

class KotlinFormatEngine {
    fun format(
        properties: Map<String, Any>,
        template: String,
    ): String {
        val engine = ScriptEngineManager().getEngineByExtension("kts")!!
        val bindings = engine.createBindings()
        for ((key, value) in properties) {
            bindings[key] = value
        }
        engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE)
        val fixedTemplate = template.trim()
            .let { if (it.last() != '"') "\"$it\"" else it }
        val evalResult = engine.eval(fixedTemplate)
        check(evalResult is String) { "Expected String, got ${evalResult?.javaClass?.canonicalName}" }
        return evalResult
    }
}

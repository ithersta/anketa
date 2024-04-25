package com.ithersta.anketa.formatting

import freemarker.cache.TemplateLoader
import freemarker.template.Configuration
import freemarker.template.SimpleObjectWrapper
import freemarker.template.Template
import java.io.StringWriter

object FormatEngine {
    private val configuration = Configuration(Configuration.VERSION_2_3_32).apply {
        objectWrapper = SimpleObjectWrapper(Configuration.VERSION_2_3_32)
    }

    fun format(
        properties: Map<String, Any>,
        templateString: String,
    ): String {
        val template = Template("template", templateString, configuration)
        val stringWriter = StringWriter()
        template.process(properties, stringWriter)
        return stringWriter.toString()
    }
}

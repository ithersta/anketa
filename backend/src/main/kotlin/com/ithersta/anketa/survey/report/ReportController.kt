package com.ithersta.anketa.survey.report

import com.ithersta.anketa.auth.userId
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Controller
class ReportController(
    private val reportService: ReportService,
    private val surveySummarizationService: SurveySummarizationService,
) {
    @PostMapping("/dashboard/survey/{id}/export/report")
    suspend fun report(
        @PathVariable("id") id: UUID,
        @RequestBody reportContent: ReportContent,
        token: UsernamePasswordAuthenticationToken,
    ): ResponseEntity<ByteArray> {
        val report = reportService.generateDocx(id, token.userId, reportContent)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        headers.set("x-filename", report.first)
        return ResponseEntity.ok().headers(headers).body(report.second)
    }

    @PostMapping("/dashboard/survey/{id}/export/summarise/{entryId}")
    suspend fun summarise(
        @PathVariable("id") id: UUID,
        @PathVariable("entryId") entryId: UUID,
        token: UsernamePasswordAuthenticationToken,
    ): ResponseEntity<String> {
        val summarization = surveySummarizationService.summarize(id, token.userId, entryId)
        return ResponseEntity.ok(summarization)
    }
}

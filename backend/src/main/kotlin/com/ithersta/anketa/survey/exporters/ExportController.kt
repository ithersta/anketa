package com.ithersta.anketa.survey.exporters

import com.ithersta.anketa.auth.userId
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@Controller
class ExportController(
    private val exportService: ExportService,
) {
    @GetMapping("/dashboard/survey/{id}/export/xlsx")
    suspend fun exportXlsx(@PathVariable id: UUID, token: UsernamePasswordAuthenticationToken): ResponseEntity<ByteArray> {
        val xlsx = exportService.generateXlsx(id, token.userId)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        headers.set(
            HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment().filename("$id.xlsx").build().toString(),
        )
        return ResponseEntity.ok().headers(headers).body(xlsx)
    }

    @GetMapping("/dashboard/survey/{id}/export/csv")
    suspend fun exportCsv(@PathVariable id: UUID, token: UsernamePasswordAuthenticationToken): ResponseEntity<ByteArray> {
        val xlsx = exportService.generateCsv(id, token.userId)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        headers.set(
            HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment().filename("$id.csv").build().toString(),
        )
        return ResponseEntity.ok().headers(headers).body(xlsx)
    }
}

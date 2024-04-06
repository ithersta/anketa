package com.ithersta.anketa.survey.exporters.xlsx

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
class XlsxExportController(
    private val xlsxExportService: XlsxExportService,
) {
    @GetMapping("/dashboard/survey/{id}/export/xlsx")
    suspend fun export(@PathVariable id: UUID, token: UsernamePasswordAuthenticationToken): ResponseEntity<ByteArray> {
        val xlsx = xlsxExportService.generateXlsx(id, token.userId)
        val headers = HttpHeaders()
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        headers.set(
            HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.attachment().filename("$id.xlsx").build().toString(),
        )
        return ResponseEntity.ok().headers(headers).body(xlsx)
    }
}

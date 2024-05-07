package com.ithersta.anketa.survey.viewer.controllers

import com.ithersta.anketa.DatabaseTest
import com.ithersta.anketa.crypto.SignedMessage
import com.ithersta.anketa.survey.domain.AnswerMap
import com.ithersta.anketa.survey.domain.entries.MultiChoiceEntry
import com.ithersta.anketa.survey.viewer.services.SurveyService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.BigIntegers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.shaded.org.bouncycastle.jce.ECPointUtil
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.Security
import java.security.Signature
import java.security.interfaces.ECPublicKey
import java.util.*

@SpringBootTest
@AutoConfigureWebTestClient
class AnswerControllerTest : DatabaseTest() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var surveyService: SurveyService

    @Test
    fun `given valid answers when POST answer then answer is saved`() {
        val surveyContent = runBlocking {
            surveyService.getPublicContentById(populator.surveyId)
        }
        val answerMap = AnswerMap(mapOf(
            surveyContent.entries.first().id to MultiChoiceEntry.Answer(selected = setOf(1), other = null)
        ))
        val requestBody = sign(Json.encodeToString(answerMap))

        webTestClient.post()
            .uri("/answer/${populator.surveyId}")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .exchange()
            .expectStatus().isOk
    }

    private fun sign(content: String): SignedMessage {
        Security.addProvider(BouncyCastleProvider())
        val encoder = Base64.getEncoder()
        val keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC")
        keyPairGenerator.initialize(SignedMessage.spec)
        val keyPair = keyPairGenerator.generateKeyPair()
        val signature = Signature.getInstance("SHA256withPLAIN-ECDSA", "BC")
        signature.initSign(keyPair.private)
        signature.update(content.encodeToByteArray())
        val signed = signature.sign()
        return SignedMessage(
            publicKey = encoder.encodeToString(encodePublicKey(keyPair.public)),
            content = content,
            signed = encoder.encodeToString(signed),
        )
    }

    private fun encodePublicKey(publicKey: PublicKey): ByteArray {
        val w = (publicKey as ECPublicKey).w
        val x = BigIntegers.asUnsignedByteArray(w.affineX)
        val y = BigIntegers.asUnsignedByteArray(w.affineY)
        val encoded = ByteArray(65)
        encoded[0] = 0x04
        for (i in x.indices) {
            encoded[i + 33 - x.size] = x[i]
        }
        for (i in y.indices) {
            encoded[i + 33 + 32 - y.size] = y[i]
        }

        return encoded
    }
}

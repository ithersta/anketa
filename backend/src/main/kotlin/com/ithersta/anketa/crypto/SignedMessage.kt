package com.ithersta.anketa.crypto

import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.*

private val logger = LoggerFactory.getLogger(SignedMessage::class.java)

@Serializable
class SignedMessage(
    val publicKey: String,
    private val content: String,
    private val signed: String,
) {
    fun getVerifiedContent(): String? = runCatching {
        val signature = Signature.getInstance("SHA256withECDSA")
        signature.initVerify(loadPublicKey())
        signature.update(content.encodeToByteArray())
        return if (signature.verify(loadSigned())) {
            content
        } else {
            null
        }
    }.onFailure { e ->
        logger.error("Couldn't verify signature", e)
    }.getOrNull()

    private fun loadSigned(): ByteArray {
        return Base64.getDecoder().decode(signed)
    }

    private fun loadPublicKey(): PublicKey {
        val data = Base64.getDecoder().decode(publicKey)
        val spec = X509EncodedKeySpec(data)
        val keyFactory = KeyFactory.getInstance("ECDSA")
        return keyFactory.generatePublic(spec)
    }
}
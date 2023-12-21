package com.ithersta.anketa.crypto

import kotlinx.serialization.Serializable
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.ECPointUtil
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import org.slf4j.LoggerFactory
import java.math.BigInteger
import java.security.AlgorithmParameters
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Security
import java.security.Signature
import java.security.spec.*
import java.util.*

private val logger = LoggerFactory.getLogger(SignedMessage::class.java)

@Serializable
class SignedMessage(
    val publicKey: String,
    private val content: String,
    private val signed: String,
) {
    fun getVerifiedContent(): String? = runCatching {
        val signature = Signature.getInstance("SHA256withPLAIN-ECDSA", "BC")
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
        val spec = ECNamedCurveTable.getParameterSpec("secp256r1")
        val params = ECNamedCurveSpec("secp256r1", spec.curve, spec.g, spec.n)
        val point = ECPointUtil.decodePoint(params.curve, data)
        val publicKeySpec = ECPublicKeySpec(point, params)
        val keyFactory = KeyFactory.getInstance("EC", "BC")
        return keyFactory.generatePublic(publicKeySpec)
    }

    companion object {
        init {
            Security.addProvider(BouncyCastleProvider())
        }
    }
}
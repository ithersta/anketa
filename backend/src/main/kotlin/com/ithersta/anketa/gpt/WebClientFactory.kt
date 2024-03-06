package com.ithersta.anketa.gpt

import io.netty.handler.ssl.SslContextBuilder
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.security.KeyStore
import javax.net.ssl.TrustManagerFactory

@Component
class WebClientFactory {
    fun build(): WebClient {
        val sslContext = this::class.java.getResourceAsStream("/truststore.jks").use { inputStream ->
            val trustStore = KeyStore.getInstance("jks")
            trustStore.load(inputStream, "anketa".toCharArray())
            val trustManagerFactory = TrustManagerFactory.getInstance("SunX509")
            trustManagerFactory.init(trustStore)
            SslContextBuilder.forClient()
                .trustManager(trustManagerFactory)
                .build()
        }
        val httpClient = HttpClient.create().secure { spec ->
            spec.sslContext(sslContext)
        }
        val clientHttpConnector = ReactorClientHttpConnector(httpClient)
        return WebClient.builder()
            .clientConnector(clientHttpConnector)
            .build()
    }
}

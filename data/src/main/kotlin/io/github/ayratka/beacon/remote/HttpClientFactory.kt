package io.github.ayratka.beacon.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(
        engine: HttpClientEngine = OkHttp.create(),
        enableLogging: Boolean = false,
    ): HttpClient = HttpClient(engine) {
        expectSuccess = false

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = REQUEST_TIMEOUT_MS
            connectTimeoutMillis = CONNECT_TIMEOUT_MS
            socketTimeoutMillis = SOCKET_TIMEOUT_MS
        }

        if (enableLogging) {
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }

    private const val REQUEST_TIMEOUT_MS = 15_000L
    private const val CONNECT_TIMEOUT_MS = 10_000L
    private const val SOCKET_TIMEOUT_MS = 15_000L
}
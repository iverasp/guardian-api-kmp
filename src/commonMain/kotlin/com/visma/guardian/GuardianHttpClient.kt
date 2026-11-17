package com.visma.guardian

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

public fun guardianHttpClient(block: HttpClientConfig<*>.() -> Unit = {}): HttpClient =
    HttpClient {
        expectSuccess = false
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                },
            )
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 60_000
            requestTimeoutMillis = 75_000
            socketTimeoutMillis = 15_000
        }
        defaultRequest {
            headers.append("Accept", "application/json")
        }
        block()
    }

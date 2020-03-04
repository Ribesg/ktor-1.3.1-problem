package com.example.app.http

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout

fun createHttpClient() = HttpClient {
    install(LogFeature)
    install(HttpTimeout) {
        requestTimeoutMillis = 1000
        connectTimeoutMillis = 1000
        socketTimeoutMillis = 1000
    }
}

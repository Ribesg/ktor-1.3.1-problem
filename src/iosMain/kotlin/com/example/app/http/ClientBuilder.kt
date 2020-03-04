package com.example.app.http

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

fun createHttpClient() = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer(
            Json(JsonConfiguration.Stable.copy(strictMode = false))
        )
    }
}

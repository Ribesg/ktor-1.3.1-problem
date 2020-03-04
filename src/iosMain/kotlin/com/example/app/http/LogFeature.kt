package com.example.app.http

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.ResponseException
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.isSuccess
import io.ktor.util.AttributeKey

class LogFeature private constructor() {

    companion object Feature : HttpClientFeature<Unit, LogFeature> {

        override val key = AttributeKey<LogFeature>("LogFeature")

        override fun prepare(block: Unit.() -> Unit) =
            LogFeature().apply { block(Unit) }

        override fun install(feature: LogFeature, scope: HttpClient) {

            // Request
            scope.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
                HttpLogger.logRequest(context)
                proceed()
            }

            // Error response
            @Suppress("EXPERIMENTAL_API_USAGE")
            scope.responsePipeline.intercept(HttpResponsePipeline.Receive) {
                try {
                    proceed()
                } catch (e: ResponseException) {
                    HttpLogger.logResponse(e.response)
                    throw e
                }
            }

            // Success response
            ResponseObserver.install(ResponseObserver { response ->
                // Intercepts on receivePipeline.Before
                if (response.status.isSuccess()) {
                    HttpLogger.logResponse(response)
                }
            }, scope)

        }

    }

}

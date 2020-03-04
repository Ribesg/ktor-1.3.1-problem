package com.example.app.http

import io.ktor.client.call.HttpClientCall
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.charset
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.String
import io.ktor.utils.io.core.readText
import io.ktor.utils.io.readRemaining
import kotlin.random.Random

object HttpLogger {

    private val REQUEST_ID = AttributeKey<String>("requestId")

    private val randomId: String
        get() = Random.nextInt(0xFFFFFF).toString(16).padStart(6, '0').toUpperCase()

    private val passwordObfuscatorRegex = "\"password\" ?: ?\".*?(?<!\\\\)\"".toRegex()

    fun logRequest(request: HttpRequestBuilder) {
        val requestId = randomId
        request.setRequestId(requestId)
        val verb = request.method.value
        val url = request.url.buildString()
        println("🌎🔼🚀 $requestId $verb $url")
        val headers = headersToString(request.headers.entries())
        if (headers != null) {
            println("Headers:\n$headers")
        }
        if (request.body is OutgoingContent) {
            val body = requestBodyToString(request.body as OutgoingContent)
            if (body != null) {
                println("Body:\n$body")
            }
        } else {
            println("Unexpected request body of type ${request.body::class.simpleName}")
        }
    }

    suspend fun logResponse(response: HttpResponse) {
        val icon = if (response.status.isSuccess()) '✅' else '❌'
        val requestId = response.call.getRequestId()
        val statusCode = response.status.value
        val url = response.call.request.url.toString()
        println("🌎🔽$icon $requestId $statusCode $url")
        val headers = headersToString(response.headers.entries())
        if (headers != null) {
            println("Headers:\n$headers")
        }
        val body = responseBodyToString(response.contentType(), response.content)
        if (body != null) {
            println("Body:\n$body")
        }
    }

    // Serializers

    private fun headersToString(headers: Set<Map.Entry<String, List<String>>>): String? =
        if (headers.isEmpty()) null else {
            headers
                .sortedBy { it.key }
                .joinToString("\n") { (key, values) ->
                    "$key: ${values.joinToString("; ")}"
                }
        }

    private fun requestBodyToString(body: OutgoingContent): String? {
        val charset = body.contentType?.charset() ?: Charsets.UTF_8
        return when (body) {

            is OutgoingContent.WriteChannelContent -> {
                "<WriteChannelContent>"
            }

            is OutgoingContent.ReadChannelContent -> {
                "<ReadChannelContent>"
            }

            is OutgoingContent.ByteArrayContent -> {
                String(body.bytes(), charset = charset)
            }

            is EmptyContent -> null

            else -> {
                println("Body is of unsupported type ${body::class.simpleName}")
                null
            }

        }?.let {
            if (it.isBlank()) {
                null
            } else {
                JsonPrettyPrinter.maybePrettyPrint(obfuscate(it))
            }
        }
    }

    private suspend fun responseBodyToString(contentType: ContentType?, body: ByteReadChannel): String? =
        body
            .readText(contentType?.charset() ?: Charsets.UTF_8)
            .let {
                if (it.isBlank()) {
                    null
                } else {
                    JsonPrettyPrinter.maybePrettyPrint(obfuscate(it))
                }
            }

    private fun obfuscate(raw: String): String =
        raw.replace(passwordObfuscatorRegex, """"password":"************"""")

    // Request id accessors

    private fun HttpRequestBuilder.setRequestId(requestId: String) {
        setAttributes { put(REQUEST_ID, requestId) }
    }

    private fun HttpClientCall.getRequestId(): String =
        request.attributes[REQUEST_ID]

    // Local utils

    private suspend fun ByteReadChannel.readText(charset: Charset): String = try {
        readRemaining().readText(charset = charset)
    } catch (t: Throwable) {
        println("Failed to read response body for HTTP logging")
        t.printStackTrace()
        ""
    }

}

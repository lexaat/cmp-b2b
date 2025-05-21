package networking

import io.ktor.client.plugins.api.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.content.OutgoingContent
import io.ktor.util.*
import io.ktor.utils.io.*


val LoggingBodyPlugin = createClientPlugin("LoggingBodyPlugin") {
    onRequest { request, _ ->
        println("➡️ [REQUEST] ${request.method.value} ${request.url}")

        request.headers.names().forEach { name ->
            val values = request.headers.getAll(name)?.joinToString() ?: ""
            println("  $name: $values")
        }

        val body = request.body
        if (body !is OutgoingContent.NoContent) {
            println("  Body: $body")
        }
    }

    onResponse { response ->
        val status = response.status
        println("⬅️ [RESPONSE] $status")

        response.headers.names().forEach { name ->
            val values = response.headers.getAll(name)?.joinToString() ?: ""
            println("  $name: $values")
        }

        val responseBody = response.bodyAsText()
        println("  Body: $responseBody")
    }
}

